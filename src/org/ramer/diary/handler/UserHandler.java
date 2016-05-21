package org.ramer.diary.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 用户控制器：验证邮箱和用户名，更新前获取用户，实时动态和通知，滚动翻页.
 *
 * @author ramer
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class UserHandler {
  @Autowired
  UserService userService;
  @Autowired
  NotifyService notifyService;
  @Autowired
  TopicService topicService;

  /**
   * 验证用户名.
   * 如果用户名存在,写入true,否者写入false
   *
   * @param user 当更新时,表示更新用户
   * @param username 当前用户输入或自动填充的用户名
   * @param response the response
   * @param session the session
   * @param map the map
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/validateUserName")
  public void validateUserName(User user, @RequestParam("username") String username,
      HttpServletResponse response, HttpSession session, Map<String, Object> map)
      throws IOException {
    System.out.println("验证用户名");
    response.setCharacterEncoding("UTF-8");
    if (username == null || username.trim().equals("")) {
      return;
    }
    //    id存在,用户更新
    if (user.getId() != null && user.getId() > 0) {
      System.out.println("用户更新: name  : " + user.getName());
      if (user.getName().equals(username)) {
        response.getWriter().write("false");
      }
    }
    if (userService.getByName(username) == null) {
      response.getWriter().write("false");
      return;
    } else {
      response.getWriter().write("true");
      return;
    }
  }

  /**
   * 验证邮箱是否可用.
   *
   * @param emailString 邮箱字符串
   * @param response the response
   * @param map the map
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping(value = "/user/validateEmail", method = RequestMethod.POST)
  public void validateEmail(@RequestParam("email") String emailString, HttpServletResponse response,
      Map<String, Object> map) throws IOException {
    emailString = emailString.trim();
    response.setCharacterEncoding("UTF-8");
    if (emailString == null || emailString.equals("")) {
      return;
    }
    if (!MailUtils.isEmail(emailString)) {
      response.getWriter().write("notEmail");
      return;
    }

    if (userService.getByEmail(Encrypt.execEncrypt(emailString, true)) == null) {
      response.getWriter().write("notExist");
      return;
    }
    System.out.println("邮箱已存在");
    response.getWriter().write("exist");
  }

  /**
   * 获取实时动态.
   *
   * @param session the session
   * @param user the user
   * @return 新动态总数
   * @throws JsonProcessingException the json processing exception
   */
  @RequestMapping("/user/realTimeTopic")
  @ResponseBody
  public String realTimeTopic(HttpSession session) throws JsonProcessingException {
    long count = topicService.getCount();
    long OldCount = (long) session.getAttribute("topicCount");
    System.out.println("新动态：" + (count - OldCount));
    return String.valueOf((count - OldCount));
  }

  /**
   * 获取实时通知.
   *
   * @param session the session
   * @param user the user
   * @return 通知数
   * @throws JsonProcessingException the json processing exception
   */
  @RequestMapping("/user/realTimeNotify")
  @ResponseBody
  public String realTimeNotify(HttpSession session) throws JsonProcessingException {
    User user = (User) session.getAttribute("user");
    Integer number = notifyService.getNotifiedNumber(user);
    System.out.println("新通知：" + number);
    return String.valueOf(number);
  }

  /**
   * 滚动翻页.
   *
   * @param session the session
   * @param response the response
   * @param scrollInPageStr the scroll in page str
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/scrollInPage")
  public void scrollInPage(HttpSession session, HttpServletResponse response,
      @RequestParam(value = "scrollInPage", required = false, defaultValue = "false") String scrollInPageStr)
      throws IOException {
    response.setCharacterEncoding("UTF-8");
    boolean scrollInPage = Boolean.parseBoolean(scrollInPageStr);
    session.setAttribute("scrollInPage", scrollInPage);
    response.getWriter().write("scrollInpage: " + scrollInPage);
  }

}
