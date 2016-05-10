package org.ramer.diary.handler.user;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 个人中心.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class PersonalMiddle {

  @Autowired
  private UserService userService;
  @Autowired
  private NotifyService notifyService;

  /**
   * 个人中心.
   *
   * @param user 用户
   * @param map the map
   * @param session the session
   * @return 如果用户已登录返回个人主页,否则返回错误页面
   */
  @RequestMapping("/user/personal")
  public String personalMiddle(User user, Map<String, Object> map, HttpSession session) {

    session.setAttribute("inOtherPage", false);
    session.setAttribute("inTopicPage", false);
    if (!UserUtils.checkLogin(session)) {
      throw new UserNotLoginException("您的登录已过期,请重新登录");
    }
    // 避免懒加载异常,重新获取user
    user = userService.getById(user.getId());
    System.out.println("个人中心");
    String hasCheck = "false";
    Set<Notify> notifies = notifyService.getNotifies(user, hasCheck);
    hasCheck = "true";
    Set<Notify> readedNotifies = notifyService.getNotifies(user, hasCheck);
    user.setNotifies(notifies);
    user.setReadedNotifies(readedNotifies);
    System.out.println(" 用户 " + user.getId() + " 收到 " + notifies.size() + "	条消息");
    map.put("user", user);
    map.put("notifyCount", notifies.size());
    return "personal";
  }

}