package org.ramer.diary.handler.user;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Reply;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.ReplyService;
import org.ramer.diary.util.UserUtils;
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
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class AddReply {

  //全局出错页面
  final String ERROR = PageConstant.ERROR.toString();

  @Autowired
  private ReplyService replyService;

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
  public String replyComment(@PathVariable("comment_id") String id,
      @RequestParam("content") String content, User user, HttpSession session,
      HttpServletResponse response, Map<String, Object> map) throws IOException {
    if (!UserUtils.checkLogin(session)) {
      throw new UserNotLoginException("您还未登录,或登录已过期,请登录");
    }
    Integer comment_id = 0;
    try {
      comment_id = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      System.out.println("格式化异常");
    }
    System.out.println(comment_id);
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
      System.out.println("在他人主页评论");
      return "redirect:/user/personal/" + other.getId();
    }
    // 在某个分享页面
    if ((boolean) session.getAttribute("inTopicPage")) {
      System.out.println("在分享页面评论");
      Topic topic = (Topic) session.getAttribute("topic");
      return "redirect:/user/topic/" + topic.getId();
    }
    System.out.println("在个人主页评论");
    return "redirect:/user/personal";

  }

  /**
   * 删除一个回复.
   *
   * @param id the id
   * @param user the user
   * @param map the map
   * @param session the session
   * @return the string
   */
  @RequestMapping("/user/topic/reply/delete/{reply_id}")
  public String deleteReply(@PathVariable("reply_id") String id, User user, Map<String, Object> map,
      HttpSession session) {
    System.out.println("删除回复");
    Integer reply_id = 0;
    try {
      reply_id = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      System.out.println("数据格式错误");
      throw new IllegalAccessException("数据格式有误");
    }
    if (reply_id != 0) {
      if (replyService.deleteReply(reply_id)) {
        if ((boolean) session.getAttribute("inOtherPage")) {
          User other = (User) session.getAttribute("other");
          System.out.println("在他人主页评论");
          return "redirect:/user/personal/" + other.getId();
        }
        // 在某个分享页面
        if ((boolean) session.getAttribute("inTopicPage")) {
          System.out.println("在分享页面评论");
          Topic topic = (Topic) session.getAttribute("topic");
          return "redirect:/user/topic/" + topic.getId();
        }
        System.out.println("在个人主页评论");
        return "redirect:/user/personal";
      }
    }
    return ERROR;

  }

}