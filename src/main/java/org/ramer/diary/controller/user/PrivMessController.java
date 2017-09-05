package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.*;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.exception.DiaryException;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.util.StringUtils;
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
    @ResponseBody
    public CommonResponse sendPrivMess(User user, @RequestParam("content") String content, HttpServletResponse response,
            HttpSession session) throws IOException {
        log.debug("发送私信");
        response.setCharacterEncoding("utf-8");
        if (!UserUtils.checkLogin()) {
            return new CommonResponse(false, "需要先登录才能说悄悄话哦");
        }
        if (StringUtils.hasText(content)) {
            return new CommonResponse(false, "消息不能为空");
        }
        //log.debug("被通知用户 = " + notifiedUser);
        Notify notify = new Notify();
        notify.setUser(user);
        notify.setNotifiedUser((User) session.getAttribute("other"));
        notify.setContent(content);
        notify.setDate(new Date());
        notify.setHasCheck("false");
        return notifyService.sendPrivMess(notify) ? new CommonResponse(false, "消息发送成功")
                : new CommonResponse(false, "系统正在打麻将,请稍后再试");
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
                throw new DiaryException("通知标记为已读失败");
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