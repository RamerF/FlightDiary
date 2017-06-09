package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

/**
 * 个人中心.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", "topicsPage" }, types = { User.class, Topic.class })
@Controller
public class PersonalMiddle{

    @Resource
    private UserService userService;
    @Resource
    private TopicService topicService;
    @Resource
    private NotifyService notifyService;
    //分享页面大小
    @Value("${diary.personal.topic.page.size}")
    private int TOPICPAGESIZE;

    /**
     * 个人中心.
     *
     * @param user 用户
     * @param map the map
     * @param session the session
     * @return 如果用户已登录返回个人主页,否则返回错误页面
     */
    @GetMapping("/user/personal")
    public String personalMiddle(@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            User user, Map<String, Object> map, HttpSession session) {

        session.setAttribute("inOtherPage", false);
        session.setAttribute("inTopicPage", false);
        if (!UserUtils.checkLogin(session)) {
            throw new UserNotLoginException("您的登录已过期,请重新登录");
        }
        // 避免懒加载异常,重新获取user
        user = userService.getById(user.getId());
        log.debug("个人中心");
        String hasCheck = "false";
        Set<Notify> notifies = notifyService.getNotifies(user, hasCheck);
        hasCheck = "true";
        Set<Notify> readedNotifies = notifyService.getNotifies(user, hasCheck);
        user.setNotifies(notifies);
        user.setReadedNotifies(readedNotifies);
        log.debug(" 用户 " + user.getId() + " 收到 " + notifies.size() + "	条消息");
        int page = 1;
        //当页面页号属于人为构造时，用于判断页号是否存在
        @SuppressWarnings("unchecked")
        Page<Topic> oldTopics = (Page<Topic>) session.getAttribute("topicsPage");
        try {
            page = Integer.parseInt(pageNum);
            if (page < 1) {
                page = 1;
            } else if (oldTopics != null && page > oldTopics.getTotalPages()) {
                page = oldTopics.getTotalPages() > 0 ? oldTopics.getTotalPages() : 1;
            }
        } catch (Exception e) {
            page = 1;
        }
        Page<Topic> topicsPage = topicService.getTopicsPageByUserId(user, page, TOPICPAGESIZE);
        map.put("topicsPage", topicsPage);
        map.put("notifyCount", notifies.size());
        map.put("user", user);
        return "personal";
    }

}