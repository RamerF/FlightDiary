package org.ramer.diary.handler.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.UserNotExistException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 浏览他人主页或分享.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class VisitOther {

  @Autowired
  private UserService userService;

  /**
   * 访问他人主页.
   *
   * @param id UID
   * @param map the map
   * @param session the session
   * @return 引导到他人主页
   */
  @RequestMapping("/user/personal/{id}")
  public String visitOtherPage(@PathVariable("id") Integer id, Map<String, Object> map,
      HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user != null && id == user.getId()) {
      //获取点赞信息
      System.out.println("访问个人空间");
      List<Integer> praises = praiseToList(user, user, session);
      session.setAttribute("praises", praises);
      map.put("user", userService.getById(id));
      return "redirect:/user/personal";
    }
    //    inOtherPage = true;

    session.setAttribute("inOtherPage", true);
    User other = userService.getById(id);
    if (other == null) {
      throw new UserNotExistException("您访问的用户不存在");
    }
    System.out.println(other.getTopics().iterator().next().getDate());
    if (UserUtils.checkLogin(session)) {
      System.out.println("已登录,写入信息");
      //获取收藏信息
      List<Integer> favourites = favouriteToList(user, other, session);
      //获取点赞信息
      List<Integer> praises = praiseToList(user, other, session);
      System.out.println("访问" + other.getName() + "的空间");
      //写入关注信息
      session.setAttribute("isFollowed", isFollowed(user, other));
      //写入收藏信息
      session.setAttribute("favourites", favourites);
      //写入收藏信息
      session.setAttribute("praises", praises);
    }
    session.setAttribute("other", other);
    return "visitOther";
  }

  /**
   * 查看某个分享详情.
   *
   * @param topic_id 分享UID
   * @param map the map
   * @param session the session
   * @return 返回某个分享的详情页面
   */
  @RequestMapping("/user/topic/{topic_id}")
  public String forwardTopic(@PathVariable("topic_id") Integer topic_id, Map<String, Object> map,
      HttpSession session) {
    //    inOtherPage = false;
    //    inTopicPage = true;

    session.setAttribute("inOtherPage", false);
    session.setAttribute("inTopicPage", true);
    //  查看分享
    System.out.println("-----查看分享-----");
    Topic topic = userService.getTopicById(topic_id);
    User user = (User) session.getAttribute("user");
    if (UserUtils.checkLogin(session)) {
      System.out.println("已登录,写入信息");
      //获取收藏信息
      List<Integer> favourites = favouriteToList(user, topic.getUser(), session);
      //获取点赞信息
      List<Integer> praises = praiseToList(user, topic.getUser(), session);

      //写入收藏信息
      session.setAttribute("favourites", favourites);
      //写入关注信息
      session.setAttribute("isFollowed", isFollowed(user, topic.getUser()));
      //写入点赞信息
      session.setAttribute("praises", praises);
    }
    map.put("topic", topic);
    session.setAttribute("other", topic.getUser());
    return "topic";
  }

  /**
   * 将指定用户收藏信息存储到list中
   * @param user 用户
   * @param topic 该用户的分享
   * @return list中存储的是当前用户收藏的所有,当前被浏览用户,的分享的id.
   */
  public List<Integer> favouriteToList(User user, User other, HttpSession session) {
    if (!UserUtils.checkLogin(session)) {
      return new ArrayList<>();
    }
    List<Integer> list = userService.getFavouriteTopicIds(user, other);
    System.out.println("已收藏分享id : ");
    for (Integer integer : list) {
      System.out.println("\t" + integer);
    }
    return list;
  }

  public List<Integer> praiseToList(User user, User other, HttpSession session) {
    if (!UserUtils.checkLogin(session)) {
      return new ArrayList<>();
    }
    List<Integer> list = userService.getPraiseTopicIds(user, other);
    System.out.println("已点赞分享id : ");
    for (Integer integer : list) {
      System.out.println("\t" + integer);
    }
    return list;
  }

  /**
   * 判断是否已关注该用户
   * @param user
   * @param followedUser
   * @return 已关注返回true
   */
  boolean isFollowed(User user, User followedUser) {
    boolean flag = userService.isFollowed(user, followedUser);
    return (flag == true) ? true : false;
  }
}