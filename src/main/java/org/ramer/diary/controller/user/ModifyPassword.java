package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.EncryptUtil;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 *修改密码.
 * @author ramer
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class ModifyPassword{

    /**
     * The User service.
     */
    @Resource
    UserService userService;
    /**
     * The Success.
     */
    //全局成功页面
    final String SUCCESS = PageConstant.SUCCESS;

    /**
     * 重定向到修改密码页面.
     *
     * @param session the session 
     * @return 引导到修改密码页面 string
     */
    @GetMapping("/user/forwardModifyPassword")
    public String forwardModifyPassword(HttpSession session) {
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            throw new UserNotLoginException("您还未登录或登录已过期");
        }
        log.debug("引导到修改用户密码页面");
        return "modify_pass";
    }

    /**
     * 修改密码.
     *
     * @param oldPassword 原始密码 
     * @param newPassword 新密码 
     * @param user 用户 
     * @param map the map 
     * @return 密码修改成功 : 返回个人主页,失败: 返回密码修改页面 
     */
    @PutMapping("/user/modifyPassword")
    public String modifyPassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, User user, Map<String, Object> map, HttpSession session) {
        if (!EncryptUtil.execEncrypt(oldPassword).equals(user.getPassword())) {
            log.debug("原始密码错误");
            map.put("error_modifyPass", "原始密码错误");
            return "modify_pass";
        }
        if (oldPassword.equals(newPassword)) {
            log.debug("密码未改变");
            session.setAttribute("succMessage", "密码修改成功");
            return SUCCESS;
        }
        user.setPassword(EncryptUtil.execEncrypt(newPassword));
        userService.newOrUpdate(user);
        if (user == null) {
            throw new SystemWrongException();
        }
        session.setAttribute("succMessage", "密码修改成功");
        return SUCCESS;
    }

}