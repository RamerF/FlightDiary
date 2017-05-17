package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.PageConstantOld;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.EmailExistException;
import org.ramer.diary.exception.LinkInvalidException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.impl.UserServiceImpl;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.MailUtils;
import org.ramer.diary.util.UserUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *修改邮箱.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class ModifyEmail{

    @Autowired
    UserServiceImpl userService;
    //全局成功页面
    final String SUCCESS = PageConstantOld.SUCCESS.toString();

    /**
     * 定向到修改邮箱页面.
     * @param session JSP内置对象
     * @return 修改邮箱页面
     */
    @RequestMapping("/user/forwardModifyEmail")
    public String forwardModifyEmail(HttpSession session) {
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            if (!UserUtils.multiLogin(session, u)) {
                throw new UserNotLoginException("账号异地登陆！ 当前登陆失效，如果不是本人操作，请及时修改密码 !");
            }
            throw new UserNotLoginException("您的登录已过期,请重新登录");
        }
        return "modifyEmail";
    }

    /**
     * 发送邮件,更改邮箱.
     *
     * @param newEmail 新邮箱
     * @param user the user
     * @param map the map
     * @param session the session
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping("/user/modifyEmail/sendMail")
    public void sendEmailToModifyEmail(@RequestParam("newEmail") String newEmail, User user, HttpSession session,
            HttpServletResponse response) throws IOException {
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            if (!UserUtils.multiLogin(session, u)) {
                throw new UserNotLoginException("账号异地登陆！ 当前登陆失效，如果不是本人操作，请及时修改密码 !");
            }
            throw new UserNotLoginException("您的登录已过期,请重新登录");
        }
        log.debug("发送邮件,修改邮箱");
        response.setCharacterEncoding("utf-8");
        if (newEmail.trim() == null || newEmail.trim().equals("")) {
            response.getWriter().write("臣妾还不知道发到哪儿呐");
            return;
        }
        if (!MailUtils.isEmail(newEmail)) {
            response.getWriter().write("您输入的不是邮箱哒 ^o^||");
            return;
        }
        //判断邮箱在数据库中是否存在
        if (MailUtils.exist(newEmail, userService)) {
            throw new EmailExistException();
        }
        Calendar calendar = Calendar.getInstance();
        //    时间是五分钟之后
        calendar.add(Calendar.MINUTE, 5);
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
        user.setExpireTime(expireTime);
        userService.newOrUpdate(user);
        String servletName = session.getServletContext().getServletContextName();
        String encodedEmail = Encrypt.execEncrypt(newEmail, true);
        String content = "<h3>请点击下面的链接完成邮箱更改,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/" + servletName
                + "/user/modifyEmail?email1=" + encodedEmail + "&email2=" + user.getEmail() + "'>http://localhost:8080/"
                + servletName + "/user/modifyEmail/" + newEmail + "</a>";
        String top = "来自旅行日记的更改邮箱邮件";
        MailUtils.sendMail(newEmail, top, content);
        response.getWriter().write("嗖.......... 到家啦 ^v^,查收邮件后再继续操作哦");
    }

    /**
     * 修改邮箱.
     *
     * @param email 用户原来的邮箱
     * @param newEmail 修改的新邮箱
     * @param map the map
     * @param session the session
     * @return the string
     */
    @RequestMapping("/user/modifyEmail")
    public String modifyEmail(@RequestParam("email2") String email, @RequestParam("email1") String newEmail,
            HttpSession session) {
        log.debug("修改邮箱");
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(Calendar.getInstance().getTime());
        User user = userService.getByEmail(email);
        if (user == null || expireTime.compareTo(user.getExpireTime()) > 0) {
            throw new LinkInvalidException();
        }
        //判断邮箱是否存在
        if (MailUtils.exist(newEmail, userService)) {
            throw new EmailExistException();
        }
        user.setEmail(newEmail);
        userService.newOrUpdate(user);
        UserUtils.execSuccess(session, "邮箱更改成功啦,去主页溜溜吧");
        user.setExpireTime(expireTime);
        return SUCCESS;
    }
}