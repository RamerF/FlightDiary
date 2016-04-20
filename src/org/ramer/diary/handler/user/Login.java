package org.ramer.diary.handler.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.UsernameOrPasswordNotMatchException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 登陆类.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class Login {
  @Autowired
  private UserService userService;

  /**
   * 用户登录.
   *
   * @param user the user
   * @param map the map
   * @param session the session
   * @return 登录成功返回主页,失败返回错误页面
   */
  @RequestMapping(value = "/user/login")
  public String userLogin(User user, Map<String, Object> map, HttpSession session) {
    System.out.println("登录");
    String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    if (user.getName().matches(regex)) {
      System.out.println("通过邮箱登录");
      user.setEmail(Encrypt.execEncrypt(user.getName(), true));
      user.setName(null);
    }
    user.setPassword(Encrypt.execEncrypt(user.getPassword(), false));
    User user2 = userService.login(user);
    if (user2.getId() != null) {
      map.put("user", user2);
      return "redirect:/home";
    }
    throw new UsernameOrPasswordNotMatchException("登录失败,用户名或密码错误");
  }

}
