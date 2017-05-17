package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.PageConstantOld;
import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Reply;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.ReplyService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 回复评论和删除回复.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class AddReply{

    //全局出错页面
    final String ERROR = PageConstantOld.ERROR.toString();

    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;

    /**
     * 回复评论.
     *
     * @param id 评论ID
     * @param content 回复内容
     * @param user 登录用户
     * @param session JSP内置对象
     * @param response JSP内置对象
     * @param map the map
     * @return 返回当前页面
     * @throws IOException 写入信息失败
     */
    @RequestMapping("/user/topic/comment/reply/{comment_id}")
    public String replyComment(@PathVariable("comment_id") String id, @RequestParam("content") String content,
            User user, HttpSession session, HttpServletResponse response) {
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            if (!UserUtils.multiLogin(session, u)) {
                throw new UserNotLoginException("账号异地登陆！ 当前登陆失效，如果不是本人操作，请及时修改密码 !");
            }
            throw new UserNotLoginException("您还未登录,或登录已过期,请登录");
        }
        Integer comment_id = 0;
        try {
            comment_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            log.debug("格式化异常");
        }
        log.debug("{}",comment_id);
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
    @RequestMapping("/user/topic/reply/delete/{reply_id}")
    public String deleteReply(@PathVariable("reply_id") String id, HttpSession session) {
        log.debug("删除回复");
        Integer reply_id = 0;
        try {
            reply_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            log.debug("数据格式错误");
            throw new IllegalAccessException("数据格式有误");
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