package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.DefaultException;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.exception.NoPictureException;
import org.ramer.diary.exception.SQLExecException;
import org.ramer.diary.service.FollowService;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.util.FileUtils;
import org.ramer.diary.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发表分享和删除分享
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class Publish{
    @Autowired
    private FollowService followService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private NotifyService notifyService;

    /**
     * 删除分享.
     *
     * @param user 用户
     * @param topic_id 分享UID
     * @param map the map
     * @param session the session
     * @return 重定向到个人页面,或返回错误页面
     * @throws IOException
     */
    @RequestMapping("/user/topic/deleteTopic/{topic_id}")
    public String deleteTopic(User user, @PathVariable("topic_id") Integer topic_id, Map<String, Object> map,
            HttpSession session) throws IOException {
        log.debug("-----删除分享-----");
        //如果访问的临时用户存在,说明当前用户在他人的分享主页
        //用户将无法执行删除
        if (map.keySet().contains("other")) {
            throw new IllegalAccessException("你没有删除该条分享的权限");
        }
        Topic topic = topicService.getTopicById(topic_id);
        topicService.deleteTopic(topic);
        boolean flag = FileUtils.deleteFile(topic, session, StringUtils.hasChinese(user.getName()));
        log.debug("-----删除图片 : " + flag + "-----");
        if (!flag) {
            log.debug("method : deleteTopic -->deleteFile : Publish.java : 60.");
            throw new DefaultException();
        }
        return "redirect:/user/personal";
    }

    /**
     * 用户发表分享.
     *
     * @param content 日记文本
     * @param tags the tags
     * @param personal the personal
     * @param file the file
     * @param session the session
     * @return 在主页发表分享返回主页,在个人页面发表分享返回个人页面
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping("/publish")
    public String publish(@RequestParam("content") String content, @RequestParam(value = "tags") String tags,
            @RequestParam(value = "personal", required = false, defaultValue = "") String personal,
            @RequestParam("picture") MultipartFile file, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("user");
        log.debug("发表日记: \n\t用户名: " + user.getName());
        Topic topic = new Topic();
        //  当用户上传文件时保存文件
        if (file.isEmpty()) {
            log.debug("未选择图片");
            throw new NoPictureException("请选择一张图片");
        }
        log.debug("保存图片");
        String pictureUrl = FileUtils.saveFile(file, session, false, StringUtils.hasChinese(user.getName()));
        topic.setPicture(pictureUrl);
        topic.setContent(content);
        topic.setDate(new Date());
        topic.setUser(user);
        topic.setUpCounts(0);

        //    考虑到用户输入的各种问题，先处理tag才能保存
        if (tags.contains("；")) {
            //如果;在开头或结尾直接去掉
            tags = tags.startsWith(";") ? tags.substring(1) : tags;
            tags = tags.endsWith(";") ? tags.substring(0, tags.length() - 1) : tags;
            tags = tags.replace("；", ";");
        }
        topic.setTags(tags);
        //保存用户经历
        topic = topicService.publish(topic);
        //为空说明sql执行出错
        if (topic.getId() == null) {
            //删除文件，写入出错信息
            FileUtils.deleteFile(topic, session, StringUtils.hasChinese(user.getName()));
            throw new SQLExecException("系统被程序猿玩儿坏啦，当前无法发表分享 ！！！");
        }
        //    获取所有关注'我'的人
        List<User> followUsers = followService.getFollowUser(user);
        //通知关注用户消息
        String message = "<a href='/" + session.getServletContext().getServletContextName() + "/user/topic/"
                + topic.getId() + "' class='readPrivMess'>我发表了新的动态，猛戳链接过去瞟一眼  </a>";
        for (User followUser : followUsers) {
            log.debug("通知用户: " + followUser.getId());
            notifyService.notifyFollowUser(user, followUser, message);
        }
        if (personal.equals("true")) {
            return "redirect:/user/personal";
        }
        return "redirect:/home";
    }
}