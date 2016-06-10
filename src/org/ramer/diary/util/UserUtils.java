package org.ramer.diary.util;

import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.domain.User;

/**
 * 用户工具类，包含常用的静态方法：
 * <strong>
 *  1.登陆检测
 *  2.操作成功返回
 *  </strong>
 * @author ramer
 *
 */
public class UserUtils {
  //默认成功信息
  private static final String SUCCESSMESSAGE = MessageConstant.SUCCESSMESSAGE.toString();

  /**
   * 操作成功.
   *
   * @param session the session
   * @param succMessage 成功信息
   */
  public static void execSuccess(HttpSession session, String... succMessage) {
    if (succMessage.length > 0) {
      session.setAttribute("succMessage", succMessage[0]);
      return;
    }
    session.setAttribute("succMessage", SUCCESSMESSAGE);
  }

  /**
   * 检测用户是否登录.
   *
   * @param session the session
   * @return 已登录返回true,否则返回false
   */
  public static boolean checkLogin(HttpSession session) {
    System.out.println("登录检测");
    if (session.getAttribute("user") != null
        && ((User) session.getAttribute("user")).getId() != null) {
      System.out.println("\t已登录");
      return true;
    }
    session.setAttribute("user", new User());
    System.out.println("\t未登录");
    return false;

  }

  /**
   * 检测用户是否异地登录.
   *
   * @param session the session
   * @return 已登录返回true,否则返回false
   */
  public static boolean multiLogin(HttpSession session, User user) {
    System.out.println("登录检测");

    if (session.getId().equals(user.getSessionid())) {
      System.out.println("\t已登录");
      return true;

    }
    session.setAttribute("user", new User());
    System.out.println("\t未登录");
    return false;

  }
}
