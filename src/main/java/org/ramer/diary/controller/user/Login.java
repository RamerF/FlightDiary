package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登陆类.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class Login{
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
    @PostMapping(value = "/logins")
    @ResponseBody
    public String userLogin(User user, Map<String, Object> map, HttpSession session) {
        UserDetails diaryAuthUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("登录,{} : {}", diaryAuthUser.getUsername(), diaryAuthUser.getPassword());
        user.setSessionid(session.getId());
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if (user.getUsername().matches(regex)) {
            log.debug("通过邮箱登录");
            user.setEmail(Encrypt.execEncrypt(user.getUsername(), true));
            user.setUsername(null);
        }
        User user2 = userService.getByName(diaryAuthUser.getUsername());
        if (user2.getId() != null) {
            map.put("user", user2);
            session.setAttribute("user", user2);
            return "success";
        }
        return "error";
    }

}
