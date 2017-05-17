package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 表单回显类：
 *  提供登陆，注册，更新的表单回显
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
@Slf4j
public class FormEcho{
    @Autowired
    private UserService userService;

    /**
     * 表单回显,用于用户注册或登录.
     *
     * @param map the map
     * @return 引导用户登录界面或注册界面
     */
    @GetMapping("/user")
    public String input(User user, Map<String, Object> map) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " 表单回显,空白用户");
        return PageConstant.USER_INPUT;
    }

    /**
     * 表单回显,用于更新用户.
     *
     * @param id UID
     * @param map the map
     * @return 引导用户更新界面
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String input(@PathVariable("id") Integer id, Map<String, Object> map) {
        log.debug("表单回显,id = " + id);
        User user = (User) map.get("user");
        if (user != null && user.getId() == id) {
            map.put("user", userService.getById(user.getId()));
            return PageConstant.USER_INPUT;
        }
        log.debug("更新用户表单回显,非法访问");
        throw new IllegalAccessException("无法访问用户信息,登录已过期");
    }

}
