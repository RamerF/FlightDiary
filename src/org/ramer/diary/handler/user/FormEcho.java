package org.ramer.diary.handler.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 表单回显类：
 *  提供登陆，注册，更新的表单回显
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class FormEcho {
  @Autowired
  private UserService userService;
  //用户表单
  private final String USERINPUT = PageConstant.USERINPUT.toString();

  /**
   * 表单回显,用于用户注册或登录.
   *
   * @param session the session
   * @param map the map
   * @return 引导用户登录界面或注册界面
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String input(HttpSession session, Map<String, Object> map) {
    System.out.println("表单回显,空白用户");
    map.put("user", new User());
    return USERINPUT;
  }

  /**
   * 表单回显,用于更新用户.
   *
   * @param session the session
   * @param id UID
   * @param map the map
   * @return 引导用户更新界面
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  public String input(HttpSession session, @PathVariable("id") Integer id,
      Map<String, Object> map) {
    System.out.println("表单回显,id = " + id);
    User user = (User) map.get("user");
    if (user != null && user.getId() == id) {
      map.put("user", userService.getById(user.getId()));
      return USERINPUT;
    }
    System.out.println("更新用户表单回显,非法访问");
    throw new IllegalAccessException("无法访问用户信息,登录已过期");

  }

}
