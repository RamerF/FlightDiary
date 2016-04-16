package org.ramer.diary.handler.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 注销.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class LogOff {

  /**
   * 注销用户.
   *
   * @param session the session
   * @param request the request
   * @param response the response
   * @param map the map
   * @return 主页
   */
  @RequestMapping("/logOff")
  public String logOff(HttpSession session, HttpServletRequest request,
      HttpServletResponse response, Map<String, Object> map) {
    System.out.println("注销");
    map.clear();
    session.invalidate();
    //	try {
    //	 request.getRequestDispatcher("/home").forward(request, response);
    //	} catch (Exception e) {
    //	 e.printStackTrace();
    //	}
    return "redirect:/home";
  }

}