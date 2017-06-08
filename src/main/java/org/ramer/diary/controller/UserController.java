package org.ramer.diary.controller;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.EncryptUtil;
import org.ramer.diary.util.IntegerUtil;
import org.ramer.diary.util.MailUtils;
import org.ramer.diary.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户控制器：验证邮箱和用户名，更新前获取用户，实时动态和通知，滚动翻页.
 *
 * @author ramer
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class UserController{
    @Resource
    private UserService userService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private TopicService topicService;

    /**
     * 验证用户名.
     * 如果用户名存在,写入true,否者写入false
     *
     * @param user 当更新时,表示更新用户
     * @param username 当前用户输入或自动填充的用户名
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @PostMapping("/validateUserName")
    @ResponseBody
    public CommonResponse validateUserName(User user, @RequestParam("username") String username) throws IOException {
        if (StringUtils.isEmpty(username)) {
            return new CommonResponse(false, "用户名为空");
        }
        //    id存在,用户更新
        if (IntegerUtil.isPositiveValue(user.getId())) {
            log.debug("用户更新: username  : {}", user.getUsername());
            if (user.getUsername().equals(username)) {
                return new CommonResponse(false, "用户名未改变");
            }
        }
        if (userService.getByName(username) == null) {
            return new CommonResponse(false, "用户名不存在");
        }
        return new CommonResponse(true, "用户名已存在");
    }

    /**
     * 验证邮箱是否可用.
     *
     * @param emailString 邮箱字符串
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @PostMapping("/validateEmail")
    @ResponseBody
    public CommonResponse validateEmail(@RequestParam("email") String emailString) throws IOException {
        if (StringUtils.isEmpty(emailString)) {
            return new CommonResponse(false, "邮箱为空");
        }
        if (!MailUtils.isEmail(emailString)) {
            return new CommonResponse(false, "邮箱格式不正确");
        }
        if (userService.getByEmail(EncryptUtil.execEncrypt(emailString)) == null) {
            return new CommonResponse(true, "邮箱不存在");
        }
        return new CommonResponse(false, "邮箱已存在");
    }

    /**
     * 获取实时动态.
     *
     * @param session the session
     * @return 新动态总数
     */
    @GetMapping("/user/realTimeTopic")
    @ResponseBody
    public String realTimeTopic(HttpSession session) {
        long count = topicService.getCount();
        long OldCount = (long) session.getAttribute("topicCount");
        log.debug("新动态：" + (count - OldCount));
        return String.valueOf((count - OldCount));
    }

    /**
     * 获取实时通知.
     *
     * @param session the session
     * @return 通知数
     */
    @GetMapping("/user/realTimeNotify")
    @ResponseBody
    public String realTimeNotify(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer number = notifyService.getNotifiedNumber(user);
        log.debug("新通知：" + number);
        return String.valueOf(number);
    }

}
