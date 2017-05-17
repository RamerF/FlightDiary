package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.PageConstantOld;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.UserUtils;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *修改密码.
 * @author ramer
 *
 */@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class ModifyPassword{

    @Autowired
    UserService userService;
    //全局成功页面
    final String SUCCESS = PageConstantOld.SUCCESS.toString();

    /**
     * 重定向到修改密码页面.
     *
     * @param map the map
     * @param session the session
     * @return 引导到修改密码页面
     */
    @RequestMapping(value = "/user/forwardModifyPassword", method = RequestMethod.GET)
    public String forwardModifyPassword(HttpSession session) {
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            if (!UserUtils.multiLogin(session, u)) {
                throw new UserNotLoginException("账号异地登陆！ 当前登陆失效，如果不是本人操作，请及时修改密码 !");
            }
            throw new UserNotLoginException("您还未登录或登录已过期");
        }
        log.debug("引导到修改用户密码页面");
        return "modifyPass";
    }

    /**
     * 修改密码.
     *
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     * @param user 用户
     * @param map the map
     * @param response the response
     * @param session the session
     * @return 密码修改成功: 返回个人主页,失败: 返回密码修改页面
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping("/user/modifyPassword")
    public String modifyPassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, User user, HttpSession session) {
        if (!Encrypt.execEncrypt(oldPassword, false).equals(user.getPassword())) {
            log.debug("原始密码错误");
            session.setAttribute("error_modifyPass", "原始密码错误");
            return "redirect:/user/forwardModifyPassword";
        }
        if (oldPassword.equals(newPassword)) {
            log.debug("密码未改变");
            session.setAttribute("error_modifyPass", "");
            session.setAttribute("succMessage", "密码修改成功");
            return SUCCESS;
        }
        user.setPassword(Encrypt.execEncrypt(newPassword, false));
        user = userService.newOrUpdate(user);
        if (user == null) {
            throw new SystemWrongException();
        }
        session.setAttribute("error_modifyPass", "");
        session.setAttribute("succMessage", "密码修改成功");
        return SUCCESS;
    }

}