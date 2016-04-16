package org.ramer.diary.handler.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.LinkInvalidException;
import org.ramer.diary.exception.PasswordNotMatchException;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.MailUtils;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 忘记密码，用于重置密码.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class ForgetPassword {
  //全局成功页面
  final String SUCCESS = PageConstant.SUCCESS.toString();

  //  密码修改成功信息
  final String SUCCESSCHANGEPASS = MessageConstant.SUCCESSCHANGEPASS.toString();

  @Autowired
  UserService userService;

  /**
   * 重定向到忘记密码页面.
   * @param email 用户绑定的邮箱
   * @param map the map
   * @param session JSP内置对象
   * @return 引导到忘记密码页面
   */
  @RequestMapping(value = "/user/forwardForgetPassword", method = RequestMethod.GET)
  public String forwardForgetPassword(
      @RequestParam(value = "email", required = false, defaultValue = "") String email,
      Map<String, Object> map, HttpSession session) {
    System.out.println("引导到忘记用户密码页面");
    if (!email.equals("") && email != null) {
      map.put("email", "email");
    }
    return "forgetPass";
  }

  /**
   * 发送邮件.
   *
   * @param email 未加密的邮箱地址
   * @param map the map
   * @param session the session
   * @param response JSP内置对象
   * @throws IOException 写入信息失败抛出IO异常
   */
  @RequestMapping("/user/forgetPass/sendMail")
  public void sendMailToResetPass(@RequestParam("email") String email, Map<String, Object> map,
      HttpSession session, HttpServletResponse response) throws IOException {
    response.setCharacterEncoding("utf-8");
    if (email.trim() == null || email.trim().equals("")) {
      response.getWriter().write("臣妾还不知道发到哪儿呐");
      return;
    }
    if (!MailUtils.isEmail(email)) {
      response.getWriter().write("您输入的不是邮箱哒 ^o^||");
      return;
    }
    String encodedEmail = Encrypt.execEncrypt(email);
    User user = userService.getByEmail(encodedEmail);
    //    发送邮件之前判断是否存在,防止用户而已发送邮件
    if (user == null) {
      response.getWriter().write("您输入的邮箱未注册哟");
      return;
    }
    String servletName = session.getServletContext().getServletContextName();
    String content = "<h3>请点击下面的链接继续重置密码,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/"
        + servletName + "/user/forwardForgetPassword?email=" + encodedEmail
        + "'>http://localhost:8080/" + servletName + "/user/forgetPassword/" + email + "</a>";
    String top = "来自飞行日记的重置密码邮件";
    MailUtils.sendMail(email, top, content);
    Calendar calendar = Calendar.getInstance();
    //		时间是五分钟之后
    calendar.add(Calendar.MINUTE, 5);
    String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
    user.setExpireTime(expireTime);
    userService.newOrUpdate(user);
    response.getWriter().write("邮件发送成功");
  }

  /**
   * 忘记密码.
   *
   * @param email 已加密邮箱
   * @param password 新密码
   * @param repassword 密码重复
   * @param map the map
   * @param session the session
   * @return 密码修改成功: 返回个人主页,失败: 返回密码修改页面
   */
  @RequestMapping("/user/forgetPassword")
  public String forgetPassword(@RequestParam("email") String email,
      @RequestParam("password") String password, @RequestParam("repassword") String repassword,
      Map<String, Object> map, HttpSession session) {

    System.out.println("忘记密码,重置密码");
    User user = userService.getByEmail(email);
    //    获取当前时间,并格式化
    String expireTime = new SimpleDateFormat("yyMMddhhmmss")
        .format(Calendar.getInstance().getTime());
    if (expireTime.compareTo(user.getExpireTime()) > 0) {
      System.out.println("链接已失效");
      throw new LinkInvalidException();
    }
    if (!password.equals(repassword)) {
      throw new PasswordNotMatchException();
    }
    user.setPassword(Encrypt.execEncrypt(password));
    if (userService.newOrUpdate(user) == null) {
      throw new SystemWrongException();
    } else {
      UserUtils.execSuccess(session, SUCCESSCHANGEPASS);
      return SUCCESS;
    }
  }

}