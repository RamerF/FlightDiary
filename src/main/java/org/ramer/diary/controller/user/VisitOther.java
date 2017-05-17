package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.UserNotExistException;
import org.ramer.diary.service.FavouriteService;
import org.ramer.diary.service.FollowService;
import org.ramer.diary.service.PraiseService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 浏览他人主页或分享.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", "topicsPage" }, types = { User.class, Topic.class })
@Controller
public class VisitOther{

    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private PraiseService praiseService;
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private FollowService followService;
    //分享页面大小
    @Value("${diary.personal.topic.page.size}")
    private int TOPICPAGESIZE;

    /**
     * 访问他人主页.
     *
     * @param id UID
     * @param map the map
     * @param session the session
     * @return 引导到他人主页
     */
    @RequestMapping("/user/personal/{id}")
    public String visitOtherPage(@PathVariable("id") Integer id,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            Map<String, Object> map, HttpSession session) {
        User user = (User) session.getAttribute("user");
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
        Page<Topic> topicsPage = topicService.getTopicsPageByUserId(userService.getById(id), page, TOPICPAGESIZE);
        log.debug("----------分享------------");
        map.put("topicsPage", topicsPage);
        session.setAttribute("inOtherPage", true);
        User other = userService.getById(id);
        if (other == null) {
            throw new UserNotExistException("您访问的用户不存在");
        }
        // && UserUtils.multiLogin(session, userService.getById(((User) session.getAttribute("user")).getId()))
        if (UserUtils.checkLogin(session)) {
            log.debug("已登录,写入信息");
            if (id == user.getId()) {
                session.setAttribute("inOtherPage", false);
                //获取点赞信息
                log.debug("访问个人空间");
                List<Integer> praises = praiseToList(user, user, session);
                session.setAttribute("praises", praises);
                map.put("user", userService.getById(id));
                return "personal";
            }
            //获取收藏信息
            List<Integer> favourites = favouriteToList(user, other, session);
            //获取点赞信息
            List<Integer> praises = praiseToList(user, other, session);
            log.debug("访问" + other.getName() + "的空间");
            //写入关注信息
            session.setAttribute("isFollowed", isFollowed(user, other));
            //写入收藏信息
            session.setAttribute("favourites", favourites);
            //写入收藏信息
            session.setAttribute("praises", praises);
        }
        session.setAttribute("other", other);
        return "visitOther";
    }

    /**
     * 查看某个分享详情.
     *
     * @param topic_id 分享UID
     * @param map the map
     * @param session the session
     * @return 返回某个分享的详情页面
     */
    @RequestMapping("/user/topic/{topic_id}")
    public String forwardTopic(@PathVariable("topic_id") Integer topic_id, Map<String, Object> map,
            HttpSession session) {
        //    inOtherPage = false;
        //    inTopicPage = true;

        session.setAttribute("inOtherPage", false);
        session.setAttribute("inTopicPage", true);
        //  查看分享
        log.debug("-----查看分享-----");
        Topic topic = topicService.getTopicById(topic_id);
        if (topic == null || topic.getId() < 0) {
            throw new UserNotExistException("您访问的页面已经乘坐2333···号灰船逃离这个星球了 -.-!");
        }
        User user = (User) session.getAttribute("user");
        if (UserUtils.checkLogin(session)
                && UserUtils.multiLogin(session, userService.getById(((User) session.getAttribute("user")).getId()))) {
            log.debug("已登录,写入信息");
            //获取收藏信息
            List<Integer> favourites = favouriteToList(user, topic.getUser(), session);
            //获取点赞信息
            List<Integer> praises = praiseToList(user, topic.getUser(), session);

            //写入收藏信息
            session.setAttribute("favourites", favourites);
            //写入关注信息
            session.setAttribute("isFollowed", isFollowed(user, topic.getUser()));
            //写入点赞信息
            session.setAttribute("praises", praises);
        }
        map.put("topic", topic);
        session.setAttribute("other", topic.getUser());
        return "topic";
    }

    /**
     * 将指定用户收藏信息存储到list中
     * @param user 用户
     * @param topic 该用户的分享
     * @return list中存储的是当前用户收藏的所有,当前被浏览用户,的分享的id.
     */
    public List<Integer> favouriteToList(User user, User other, HttpSession session) {
        if (!UserUtils.checkLogin(session)
                || !UserUtils.multiLogin(session, userService.getById(((User) session.getAttribute("user")).getId()))) {
            return new ArrayList<>();
        }
        List<Integer> list = favouriteService.getFavouriteTopicIds(user, other);
        log.debug("已收藏分享id : ");
        for (Integer integer : list) {
            log.debug("\t" + integer);
        }
        return list;
    }

    /**
     * 将指定用户点赞信息存储到list中.
     *
     * @param user the user
     * @param other the other
     * @param session the session
     * @return list中存储的是当前用户点过赞的所有,当前被浏览用户,的分享的id.
     */
    public List<Integer> praiseToList(User user, User other, HttpSession session) {
        if (!UserUtils.checkLogin(session)
                || !UserUtils.multiLogin(session, userService.getById(((User) session.getAttribute("user")).getId()))) {
            return new ArrayList<>();
        }
        List<Integer> list = praiseService.getPraiseTopicIds(user, other);
        log.debug("已点赞分享id : ");
        for (Integer integer : list) {
            log.debug("\t" + integer);
        }
        return list;
    }

    /**
     * 判断是否已关注该用户
     * @param user
     * @param followedUser
     * @return 已关注返回true
     */
    boolean isFollowed(User user, User followedUser) {
        boolean flag = followService.isFollowed(user, followedUser);
        return (flag == true) ? true : false;
    }
}