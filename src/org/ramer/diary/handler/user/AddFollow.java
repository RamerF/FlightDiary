package org.ramer.diary.handler.user;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.FollowService;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 添加关注和取消关注.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class AddFollow {

  @Autowired
  private FollowService followService;

  /**
   * 添加关注.
   *
   * @param user 登陆用户
   * @param topic 用户查看的分享
   * @param map the map
   * @param response the response
   * @param session the session
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/topic/follow")
  public void follow(User user, Topic topic, Map<String, Object> map, HttpServletResponse response,
      HttpSession session) throws IOException {
    System.out.println("添加关注");
    response.setCharacterEncoding("UTF-8");
    if (!UserUtils.checkLogin(session)) {
      System.out.println("用户未登录");
      response.getWriter().write("主人说没登录不能关注哒");
      return;
    }
    User followedUser = new User();
    followedUser = (topic.getId() == null) ? (User) session.getAttribute("other") : topic.getUser();
    System.out.println("topic: " + topic.getContent());
    System.out.println("被关注用户: " + followedUser.getName());
    // 虽然在访问他人的主页时,topic没有显示写入到map中,但是在页面EL和foreach迭代输出的时候,产生了topic,
    //springmvc会将此topic封装,因此这里的topic是最后一个被迭代的topic对象
    boolean flag = followService.follow(user, followedUser);
    if (flag) {
      response.getWriter().write("关注成功 !");
      return;
    }
    response.getWriter().write("你已经关注过他/她了 !");
  }

  /**
   * 取消关注用户.
   *
   * @param topic  查看的分享
   * @param map the map
   * @param response jsp内置对象
   * @param session  the session
   * @throws IOException 写入信息失败抛出异常
   */
  @RequestMapping("/user/topic/notFollow")
  public void notFollow(Topic topic, Map<String, Object> map, HttpServletResponse response,
      HttpSession session) throws IOException {
    System.out.println("取消关注");
    response.setCharacterEncoding("UTF-8");
    if (!UserUtils.checkLogin(session)) {
      System.out.println("用户未登录");
      response.getWriter().write("请先登录再继续操作");
      return;
    }
    User user = (User) session.getAttribute("user");
    User followedUser = new User();
    followedUser = (topic.getId() == null) ? (User) session.getAttribute("other") : topic.getUser();
    System.out.println("被关注用户: " + followedUser.getName());
    // 虽然在访问他人的主页时,topic没有显示写入到map中,但是在页面EL和foreach迭代输出的时候,产生了topic,
    //springmvc会将此topic封装,因此这里的topic是最后一个被迭代的topic对象
    boolean flag = followService.notFollow(user, followedUser);
    if (flag) {
      response.getWriter().write("取消关注成功 !");
      return;
    }
    response.getWriter().write("取消失败 !");
  }

}