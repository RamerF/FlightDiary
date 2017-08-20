package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.exception.DiaryException;
import org.ramer.diary.service.*;
import org.ramer.diary.util.FileUtils;
import org.ramer.diary.util.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 发表分享和删除分享
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class TopicController{
    @Resource
    private FollowService followService;
    @Resource
    private TopicService topicService;
    @Resource
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
    @DeleteMapping("/user/topic/deleteTopic/{topic_id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteTopic(User user, @PathVariable("topic_id") Integer topic_id, Map<String, Object> map,
            HttpSession session) throws IOException {
        log.debug("-----删除分享-----");
        //如果访问的临时用户存在,说明当前用户在他人的分享主页
        //用户将无法执行删除
        if (map.keySet().contains("other")) {
            throw new DiaryException("你没有删除该条分享的权限");
        }
        Topic topic = topicService.getTopicById(topic_id);
        topicService.deleteTopic(topic);
        boolean flag = FileUtils.deleteFile(topic, session, StringUtils.hasChinese(user.getUsername()));
        log.debug("-----删除图片 : " + flag + "-----");
        if (!flag) {
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " delete picture");
            throw new DiaryException();
        }
        return "redirect:/user/personal";
    }

    /**
     * 用户发表分享.
     *
     * @param content 日记文本
     * @return 在主页发表分享返回主页,在个人页面发表分享返回个人页面
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @PostMapping("/user/publish")
    @ResponseBody
    public CommonResponse publishTopic(@RequestParam("content") String content, @RequestParam("tags[]") String[] tags,
            @RequestParam("fileUrls[]") String[] fileUrls, @SessionAttribute("user") User user, HttpSession session) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "  发表新推文: [{}]", user.getUsername());
        //保存用户经历
        Topic topic = topicService.publish(user, content, tags, fileUrls);
        if (topic == null) {
            return new CommonResponse(false, "系统繁忙,请稍后再试");
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
        return new CommonResponse(true, "发表成功");
    }

    @GetMapping("/user/upload")
    public String fileUpload() {
        return "file_upload";
    }
}