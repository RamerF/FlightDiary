package org.ramer.diary.controller;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.FeedBack;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.service.FollowService;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.CollectionsUtils;
import org.ramer.diary.util.Pagination;
import org.ramer.diary.util.StringUtils;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定位到主页.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", "topicCount", "scrollInPage" }, types = { User.class, Topic.class })
@Controller
public class ForwardHome{
    @Resource
    private UserService userService;
    @Resource
    private TopicService topicService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private FollowService followService;

    //主页面
    private final String HOME = PageConstant.HOME;
    //初始化在他人主页变量
    private boolean inOtherPage = false;
    // 初始化在分享页面变量
    private boolean inTopicPage = false;
    //数据格式错误信息
    private final String WRONG_FORMAT = MessageConstant.WRONG_FORMAT;
    //分享页面大小
    @Value("${diary.topic.page.size}")
    private int TOPIC_PAGE_SIZE;
    //达人页面大小
    @Value("${diary.topPeople.page.size}")
    private int PEOPLE_PAGE_SIZE;
    //是否支持滚动翻页
    @Value("${diary.page.scroll}")
    private boolean SCROLL_IN_PAGE;

    /**
     * 主页.
     *
     * @param pageNum 页号
     * @param map the map
     * @param session the session
     * @return 引导到主页
     */
    @GetMapping("/home")
    public String home(@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            Map<String, Object> map, @SessionAttribute(name = "scrollInPage", required = false) String scrollInPage,
            HttpSession session) {
        log.debug("主页");
        //初始化滚动翻页
        if (map.get("scrollInPage") == null) {
            map.put("scrollInPage", SCROLL_IN_PAGE);
        }
        int page = 1;
        //重置标识信息
        map.put("inOtherPage", inOtherPage);
        map.put("inTopicPage", inTopicPage);
        //当页面页号属于人为构造时，用于判断页号是否存在
        @SuppressWarnings("unchecked")
        Page<Topic> oldTopics = (Page<Topic>) session.getAttribute("topics");
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
        //获取分页分享
        Page<Topic> topics = topicService.getTopicsPage(page, TOPIC_PAGE_SIZE);
        //记录最新的topicid，用于判断是否有新动态
        map.put("topicCount", topicService.getCount());
        map.put("topics", topics);
        if (UserUtils.checkLogin(session)) {
            User user = (User) session.getAttribute("user");
            //获取用户统计数据
            int notifiedNumber = notifyService.getNotifiedNumber(user);
            int topicNumber = topicService.getTopicNumber(user);
            int followedNumber = followService.getFollowedNumber(user);
            log.debug("获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
            map.put("notifiedNumber", notifiedNumber);
            map.put("topicNumber", topicNumber);
            map.put("followedNumber", followedNumber);
        }
        //清除访问的临时用户信息
        map.remove("other");
        map.remove("topic");
        session.removeAttribute("details");
        //  标识为显示分享分类
        map.put("showTopic", "true");
        //  取消标识为达人分类
        map.put("showTopPeople", "false");
        //  取消标识为热门标签分类
        map.put("showPopularTags", "false");
        return HOME;
    }

    /**
     * 主页: 分享按热度排序.
     *
     * @param pageNum 页号
     * @param map the map
     * @param session the session
     * @return 引导到主页
     */
    @GetMapping("/home/orderbyUpCounts")
    public String homeTopicOrderByUpcounts(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            Map<String, Object> map, HttpSession session) {
        //初始化滚动翻页
        if (session.getAttribute("scrollInPage") == null) {
            session.setAttribute("scrollInPage", SCROLL_IN_PAGE);
        }

        log.debug("热门主页");
        int page = 1;
        session.setAttribute("inOtherPage", false);
        session.setAttribute("inTopicPage", false);
        try {
            page = Integer.parseInt(pageNum);
            if (page < 1) {
                page = 1;
            }
        } catch (Exception e) {
            throw new IllegalAccessException("非法参数");
        }
        //获取分页分享
        Page<Topic> topics = topicService.getTopicsPageOrderByFavourite(page, TOPIC_PAGE_SIZE);
        map.put("topics", topics);
        if (UserUtils.checkLogin(session)) {
            User user = (User) session.getAttribute("user");
            //获取用户统计数据
            int notifiedNumber = notifyService.getNotifiedNumber(user);
            int topicNumber = topicService.getTopicNumber(user);
            int followedNumber = followService.getFollowedNumber(user);
            log.debug("获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
            map.put("notifiedNumber", notifiedNumber);
            map.put("topicNumber", topicNumber);
            map.put("followedNumber", followedNumber);
        }
        //清除访问的临时用户信息
        map.remove("other");
        map.remove("topic");
        session.removeAttribute("details");
        //    标识为显示分享分类
        map.put("showTopic", "true");
        //  取消标识为达人分类
        map.put("showTopPeople", "false");
        //    取消标识为热门标签分类
        map.put("showPopularTags", "false");
        return HOME;
    }

    /**
     * 达人
     * @return
     */
    @GetMapping("/home/topPeople")
    public String homeTopPeople(@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            Map<String, Object> map, HttpSession session) {
        //初始化滚动翻页
        if (session.getAttribute("scrollInPage") == null) {
            session.setAttribute("scrollInPage", SCROLL_IN_PAGE);
        }

        log.debug("达人主页");
        int page = 1;
        try {
            page = Integer.parseInt(pageNum);
            if (page < 1) {
                page = 1;
            }
        } catch (Exception e) {
            throw new IllegalAccessException(WRONG_FORMAT);
        }
        //    获取达人的分页信息
        Pagination<User> topPeoples = userService.getTopPeople(page, PEOPLE_PAGE_SIZE);
        map.put("topPeoples", topPeoples);
        if (UserUtils.checkLogin(session)) {
            User user = (User) session.getAttribute("user");
            //获取用户统计数据
            int notifiedNumber = notifyService.getNotifiedNumber(user);
            int topicNumber = topicService.getTopicNumber(user);
            int followedNumber = followService.getFollowedNumber(user);
            log.debug("获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
            map.put("notifiedNumber", notifiedNumber);
            map.put("topicNumber", topicNumber);
            map.put("followedNumber", followedNumber);
        }
        map.remove("other");
        map.remove("topic");
        session.removeAttribute("details");
        //    标识为达人分类
        map.put("showTopPeople", "true");
        //    取消标识为分享分类
        map.put("showTopic", "false");
        //    取消标识为热门标签分类
        map.put("showPopularTags", "false");
        return HOME;
    }

    /**
     * 热门标签:
     *  先获取所有标签，剔除重复的标签，按统计的次数排序,
     *  取得第一个标签名,并获取对应的分享.
     *  通过标签获取分享.
     * @param pageNum 页号
     * @param session
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/home/tag", method = RequestMethod.GET)
    public String homeTagsGetTopicsByTag(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "tag", required = false, defaultValue = "default") String tag, HttpSession session,
            Map<String, Object> map) throws UnsupportedEncodingException {
        //初始化滚动翻页
        if (session.getAttribute("scrollInPage") == null) {
            session.setAttribute("scrollInPage", SCROLL_IN_PAGE);
        }

        log.debug("热门标签主页");
        int page = 1;

        session.setAttribute("inOtherPage", false);
        session.setAttribute("inTopicPage", false);
        try {
            page = Integer.parseInt(pageNum);
            if (page < 1) {
                page = 1;
            }
        } catch (Exception e) {
            page = 1;
        }
        if (UserUtils.checkLogin(session)) {
            User user = (User) session.getAttribute("user");
            //获取用户统计数据
            int notifiedNumber = notifyService.getNotifiedNumber(user);
            int topicNumber = topicService.getTopicNumber(user);
            int followedNumber = followService.getFollowedNumber(user);
            log.debug("获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
            map.put("notifiedNumber", notifiedNumber);
            map.put("topicNumber", topicNumber);
            map.put("followedNumber", followedNumber);
        }
        //    获取数据库中所有的标签
        List<String> tags = topicService.getAllTags();
        //    去除重复的标签
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : tags) {
            stringBuilder.append(string + ";");
        }
        tags = CollectionsUtils.removeSame(Arrays.asList(stringBuilder.toString().split(";")));

        Pagination<Topic> tagTopics;
        //    取得第一个标签的分享数据
        if (tag.equals("default")) {
            tagTopics = topicService.getTopicsPageByTags(tags.iterator().next(), page, TOPIC_PAGE_SIZE);
        } else {
            tag = java.net.URLDecoder.decode(tag, "utf8");
            tagTopics = topicService.getTopicsPageByTags(tag, page, TOPIC_PAGE_SIZE);
        }
        //   将所有标签写入session
        map.put("tags", tags);
        //    将第一个标签对应的分页分享写入session
        map.put("tagTopics", tagTopics);
        //    取消标识为达人分类
        map.put("showTopPeople", "false");
        //    取消标识为分享分类
        map.put("showTopic", "false");
        //    标识为热门标签分类
        map.put("showPopularTags", "true");
        return HOME;
    }

    /**
     * 滚动翻页.
     *
     * @param session the session
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @GetMapping("/scrollInPage")
    @ResponseBody
    public CommonResponse scrollInPage(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            @SessionAttribute(name = "scrollInPage", required = false) boolean scrollInPage, Map<String, Object> map) {
        map.put("scrollInPage", !scrollInPage);
        return new CommonResponse(true, !scrollInPage ? "允许滚动翻页" : "禁止滚动翻页");
    }

    @GetMapping("/feedback")
    public String forwardFeedback() {
        return "feedback";
    }

    /**
     * 用户反馈.
     *
     * @param session the session
     * @param os the os
     * @param browser the browser
     * @param content the content
     * @throws IOException
     */
    @PostMapping("/user/feedback")
    @ResponseBody
    public CommonResponse feedback(HttpSession session, @RequestParam("OS") String os,
            @RequestParam("Browser") String browser, @RequestParam("content") String content) throws IOException {
        FeedBack feedBack = new FeedBack();
        feedBack.setDate(new Date());
        feedBack.setUser((User) session.getAttribute("user"));
        feedBack.setHasCheck("false");
        feedBack.setContent(StringUtils.concat(content, " 系统信息： ", os, " 浏览器： ", browser));
        boolean flag = userService.feedback(feedBack);
        if (flag) {
            return new CommonResponse(true, "反馈成功");
        }
        return new CommonResponse(false, "系统繁忙，请稍后再试");
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }
}
