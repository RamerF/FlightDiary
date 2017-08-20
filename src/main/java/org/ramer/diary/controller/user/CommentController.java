package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Reply;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.DiaryException;
import org.ramer.diary.service.CommentService;
import org.ramer.diary.service.ReplyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
public class CommentController{

    @Resource
    private CommentService commentService;
    @Resource
    private TopicService topicService;
    @Resource
    private UserService userService;
    @Resource
    private ReplyService replyService;
    private static final String ERROR = PageConstant.ERROR;

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
    public String comment(@PathVariable("topic_id") Integer topic_id, @RequestParam("content") String content,
            Map<String, Object> map, HttpSession session) {
        log.debug("用户评论");
        if (!UserUtils.checkLogin()) {
            User user = userService.getById(((User) session.getAttribute("user")).getId());
            throw new DiaryException("要先登录,才能评论哦 !");
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
    public String deleteComment(@PathVariable("comment_id") String comment_id, @RequestParam("topic") String topic_id,
            Map<String, Object> map, HttpSession session) {
        log.debug("-----删除某个评论-----");
        //如果在他人页面,代码属于人为构造
        //用户将无法执行删除
        if ((boolean) session.getAttribute("inOtherPage")) {
            throw new DiaryException("你没有删除该条评论的权限");
        }
        Comment comment = new Comment();
        Topic topic = new Topic();
        //防止用户非法传入topic参数
        comment.setId(Integer.parseInt(comment_id));
        topic.setId(Integer.parseInt(topic_id));
        // 如果在分享页面,判断是否为本人的分享,非本人的分享将无法删除
        if ((boolean) session.getAttribute("inTopicPage")
                && !topicService.getTopicByUserIdAndTopicId(topic.getId(), (User) map.get("user"))) {
            throw new DiaryException("你没有删除该条评论的权限");
        }
        comment.setTopic(topic);
        if (!commentService.deleteComment(comment)) {
            throw new DiaryException();
        }
        return "redirect:/user/personal";
    }

    /**
     * 回复评论.
     *
     * @param id 评论ID
     * @param content 回复内容
     * @param user 登录用户
     * @param session JSP内置对象
     * @param response JSP内置对象
     * @return 返回当前页面
     */
    @PutMapping("/user/topic/comment/reply/{comment_id}")
    public String replyComment(@PathVariable("comment_id") String id, @RequestParam("content") String content,
            User user, HttpSession session, HttpServletResponse response) {
        if (!UserUtils.checkLogin()) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            throw new DiaryException("您还未登录,或登录已过期,请登录");
        }
        Integer comment_id = 0;
        try {
            comment_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            log.debug("格式化异常");
        }
        log.debug("{}", comment_id);
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setDate(new Date());
        reply.setUser(user);
        Comment comment = new Comment();
        comment.setId(comment_id);
        reply.setComment(comment);
        response.setCharacterEncoding("utf-8");
        if (!replyService.replyComment(reply)) {
            return ERROR;
        }
        if ((boolean) session.getAttribute("inOtherPage")) {
            User other = (User) session.getAttribute("other");
            log.debug("在他人主页评论");
            return "redirect:/user/personal/" + other.getId();
        }
        // 在某个分享页面
        if ((boolean) session.getAttribute("inTopicPage")) {
            log.debug("在分享页面评论");
            Topic topic = (Topic) session.getAttribute("topic");
            return "redirect:/user/topic/" + topic.getId();
        }
        log.debug("在个人主页评论");
        return "redirect:/user/personal";

    }

    /**
     * 删除一个回复.
     *
     * @param id the id
     * @param session the session
     * @return the string
     */
    @DeleteMapping("/user/topic/reply/delete/{reply_id}")
    public String deleteReply(@PathVariable("reply_id") String id, HttpSession session) {
        log.debug("删除回复");
        Integer reply_id = 0;
        try {
            reply_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            log.debug("数据格式错误");
            throw new DiaryException("数据格式有误");
        }
        if (reply_id != 0) {
            if (replyService.deleteReply(reply_id)) {
                if ((boolean) session.getAttribute("inOtherPage")) {
                    User other = (User) session.getAttribute("other");
                    log.debug("在他人主页评论");
                    return "redirect:/user/personal/" + other.getId();
                }
                // 在某个分享页面
                if ((boolean) session.getAttribute("inTopicPage")) {
                    log.debug("在分享页面评论");
                    Topic topic = (Topic) session.getAttribute("topic");
                    return "redirect:/user/topic/" + topic.getId();
                }
                log.debug("在个人主页评论");
                return "redirect:/user/personal";
            }
        }
        return ERROR;

    }

}