package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.CommentService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * 评论和删除评论.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class AddComment{

    @Autowired
    private CommentService commentService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;

    /**
     * 发表用户评论.
     *
     * @param topic_id 分享UID
     * @param content 分享内容
     * @param map the map
     * @param session the session
     * @return 返回到他人主页或某个分享页面
     */
    @PostMapping("/user/topic/comment/{topic_id}")
    public String comment(@PathVariable("topic_id") Integer topic_id, String content,
            Map<String, Object> map, HttpSession session) {
        log.debug("用户评论");
        if (!UserUtils.checkLogin(session)) {
            User user = userService.getById(((User) session.getAttribute("user")).getId());
            throw new UserNotLoginException("要先登录,才能评论哦 !");
        }
        log.debug("-----用户评论-----");
        log.debug("评论内容：" + content);
        User user = (User) map.get("user");
        Topic topic = new Topic();
        Comment comment = new Comment();
        comment.setContent(content);
        topic.setId(topic_id);
        //防止用户恶意修改页面userid导致程序崩溃
        comment.setUser(user);
        comment.setTopic(topic);
        comment.setDate(new Date());
        commentService.comment(comment);
        //在他人主页
        if ((boolean) session.getAttribute("inOtherPage")) {
            User other = (User) session.getAttribute("other");
            log.debug("在他人主页评论");
            return "redirect:/user/personal/" + other.getId();
        }
        // 在某个分享页面
        if ((boolean) session.getAttribute("inTopicPage")) {
            log.debug("在分享页面评论");
            return "redirect:/user/topic/" + topic_id;
        }
        log.debug("在个人主页评论");
        return "redirect:/user/personal";
    }

    /**
     * 删除某个评论.
     *
     * @param comment_id 评论UID
     * @param topic_id 分享UID
     * @param map the map
     * @param session the session
     * @return 重定向到个人页面
     */
    @DeleteMapping("/user/topic/comment/delete/{comment_id}")
    public String deleteComment(@PathVariable("comment_id") String comment_id,String topic_id,
            Map<String, Object> map, HttpSession session) {
        log.debug("-----删除某个评论-----");
        //如果在他人页面,代码属于人为构造
        //用户将无法执行删除
        if ((boolean) session.getAttribute("inOtherPage")) {
            throw new IllegalAccessException("你没有删除该条评论的权限");
        }
        Comment comment = new Comment();
        Topic topic = new Topic();
        //防止用户非法传入topic参数
        comment.setId(Integer.parseInt(comment_id));
        topic.setId(Integer.parseInt(topic_id));
        // 如果在分享页面,判断是否为本人的分享,非本人的分享将无法删除
        if ((boolean) session.getAttribute("inTopicPage")
                && !topicService.getTopicByUserIdAndTopicId(topic.getId(), (User) map.get("user"))) {
            throw new IllegalAccessException("你没有删除该条评论的权限");
        }
        comment.setTopic(topic);
        if (!commentService.deleteComment(comment)) {
            throw new SystemWrongException();
        }
        return "redirect:/user/personal";
    }

}