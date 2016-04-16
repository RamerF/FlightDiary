package org.ramer.diary.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 用户控制器：验证邮箱和用户名，更新前获取用户.
 *
 * @author ramer
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class UserHandler {
  @Autowired
  UserService userService;

  /**
   * 更新前获取user.
   *
   * @param id UID
   * @param map the map
   */
  @ModelAttribute
  public void getUser(@RequestParam(value = "id", required = false) Integer id,
      Map<String, Object> map) {
    if (id != null) {
      System.out.println("更新前获取user");
      System.out.println("id = " + id);
      User user = userService.getById(id);
      map.put("user", user);
    }
  }

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
    String result = "";
    response.setCharacterEncoding("UTF-8");
    if (emailString == null || emailString.trim().equals("")) {
      return;
    }
    if (!MailUtils.isEmail(emailString)) {
      result = "<img class='valid' src='../pictures/wrong.png' weight='10px' height='10px'>";
      response.getWriter().write(result);
      return;
    }

    if (userService.getByEmail(emailString) == null) {
      result = "<img class='valid' src='../pictures/right.png' weight='10px' height='10px'>";
      response.getWriter().write(result);
      return;
    }
    System.out.println("邮箱已存在");
    result = "<img class='valid' src='../pictures/wrong.png' weight='10px' height='10px'>";
    response.getWriter().write(result);
  }

}
