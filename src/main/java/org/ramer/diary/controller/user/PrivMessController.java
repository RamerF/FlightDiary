package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.DiaryException;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 发送私信和读取私信.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class PrivMessController{

    @Resource
    private NotifyService notifyService;
    @Resource
    private UserService userService;

    /**
     * 发送私信.
     *
     * @param user 用户
     * @param content 私信内容
     * @param response the response
     * @param session the session
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @PostMapping("/user/personal/sendPrivMess")
    public void sendPrivMess(User user, @RequestParam("content") String content, HttpServletResponse response,
            HttpSession session) throws IOException {
        log.debug("发送私信");
        response.setCharacterEncoding("utf-8");
        if (!UserUtils.checkLogin(session)) {
            response.getWriter().write("需要先登录才能说悄悄话哦");
            log.debug("未登录");
            return;
        }
        if (content.trim() == null || content.trim() == "") {
            log.debug("消息为空");
            response.getWriter().write("消息不能为空");
            return;
        }
        User notifiedUser = new User();
        notifiedUser = (User) session.getAttribute("other");
        //log.debug("被通知用户 = " + notifiedUser);
        Notify notify = new Notify();
        notify.setUser(user);
        notify.setNotifiedUser(notifiedUser);
        notify.setContent(content);
        notify.setDate(new Date());
        notify.setHasCheck("false");
        boolean flag = notifyService.sendPrivMess(notify);
        response.setCharacterEncoding("utf-8");
        if (!flag) {
            log.debug("消息发送失败");
            response.getWriter().write("系统正在打麻将,请稍后再试");
            return;
        }
        log.debug("消息发送成功");
        response.getWriter().write("消息发送成功");
    }

    /**
     * 用户查看消息.
     *
     * @param notifyId 信息UID
     * @return 重定向到个人主页
     */
    @PutMapping("/user/personal/notify/readPrivMess")
    public String readPrivMess(@RequestParam("notifyId") String notifyId) {
        log.debug("读取通知 : " + notifyId);
        Notify notify = new Notify();
        Integer notify_id = -1;
        try {
            notify_id = Integer.parseInt(notifyId);
        } catch (Exception e) {
            throw new DiaryException("数据格式有误");
        }
        if (notify_id > 0) {
            notify.setId(notify_id);
            notify = notifyService.getNotifyById(notify);
            notify.setHasCheck("true");
            boolean flag = notifyService.updateNotify(notify);
            if (!flag) {
                throw new DiaryException();
            }
        } else {
            throw new DiaryException("数据格式有误");
        }
        return "redirect:/user/personal";
    }

    /**
     * 删除消息/通知
     *
     * @param notifyId the notify id
     * @param notifiedUserId the notified user id
     * @return the string
     */
    @DeleteMapping("/user/personal/notify/delete")
    @ResponseBody
    public String delete(@RequestParam("notifyId") String notifyId,
            @RequestParam("notifiedUserId") String notifiedUserId) {
        log.debug("删除消息 : " + notifyId);
        Notify notify = new Notify();
        Integer notify_id = -1;
        Integer notified_user_id = -1;
        try {
            notify_id = Integer.parseInt(notifyId);
            notified_user_id = Integer.parseInt(notifiedUserId);
        } catch (Exception e) {
            throw new DiaryException("数据格式有误");
        }
        if (notify_id > 0) {
            notify.setId(notify_id);
            notify = notifyService.getNotifyById(notify);
            User notifiedUser = new User();
            notifiedUser.setId(notified_user_id);
            notify = notifyService.getByIdAndNotifiedUserId(notify_id, notifiedUser);
            if (notify != null) {
                if (!notifyService.delete(notify)) {
                    throw new DiaryException();
                }
            }
        } else {
            throw new DiaryException("数据格式有误");
        }
        return "success";
    }

}