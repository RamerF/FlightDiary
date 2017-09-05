package org.ramer.diary.controller;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.*;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.domain.map.UserRoleMap;
import org.ramer.diary.exception.DiaryException;
import org.ramer.diary.service.*;
import org.ramer.diary.util.*;
import org.ramer.diary.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定位到主页.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", "topicCount", "scrollInPage" }, types = { User.class, Topic.class })
@Controller
public class CommonController{
    @Resource
    private UserService userService;
    @Resource
    private TopicService topicService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private FollowService followService;
    @Resource
    private TagsService tagsService;

    //初始化在他人主页变量
    private boolean inOtherPage = false;
    // 初始化在分享页面变量
    private boolean inTopicPage = false;
    //数据格式错误信息
    private final String WRONG_FORMAT = MessageConstant.WRONG_FORMAT;
    // 发表分享时显示的tags数
    @Value("${diary.topic.publish.tags.size}")
    private int TAGS_PUBLISH_SIZE;
    //分享页面大小
    @Value("${diary.topic.page.size}")
    private int TOPIC_PAGE_SIZE;
    //达人页面大小
    @Value("${diary.topPeople.page.size}")
    private int PEOPLE_PAGE_SIZE;
    //是否支持滚动翻页
    @Value("${diary.page.scroll}")
    private boolean SCROLL_IN_PAGE;
    @Resource
    private RolesService rolesService;
    @Resource
    private UserValidator userValidator;

    @InitBinder("user")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    /**
     * 主页.
     *
     * @param pageNum 页号
     * @param map the map
     * @return 引导到主页
     */
    @GetMapping("/home")
    public String home(@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            Map<String, Object> map, @SessionAttribute(name = "scrollInPage", required = false) String scrollInPage,
            @SessionAttribute(value = "topics", required = false) Page<Topic> oldTopics,
            @SessionAttribute(value = "user", required = false) User user) {
        List<Tags> tagsPage = tagsService.getTagsPage(0, TAGS_PUBLISH_SIZE);
        map.put("tags", tagsPage);
        //初始化滚动翻页
        if (map.get("scrollInPage") == null) {
            map.put("scrollInPage", SCROLL_IN_PAGE);
        }
        int page = 1;
        //重置标识信息
        map.put("inOtherPage", inOtherPage);
        map.put("inTopicPage", inTopicPage);
        //当页面页号属于人为构造时，用于判断页号是否存在
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
        if (UserUtils.checkLogin()) {
            //获取用户统计数据
            int notifiedNumber = notifyService.getNotifiedNumber(user);
            int topicNumber = topicService.getTopicNumber(user);
            int followedNumber = followService.getFollowedNumber(user);
            log.info("method[" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + "]:获取统计数据: notifiedNumber[{}],topicNumber[{}]", notifiedNumber, topicNumber);
            map.put("notifiedNumber", notifiedNumber);
            map.put("topicNumber", topicNumber);
            map.put("followedNumber", followedNumber);
        }
        //清除访问的临时用户信息
        map.remove("other");
        map.remove("topic");
        map.remove("details");
        //  标识为显示分享分类
        map.put("showTopic", true);
        //  取消标识为达人分类
        map.put("showTopPeople", false);
        //  取消标识为热门标签分类
        map.put("showPopularTags", false);
        return PageConstant.HOME;
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
            throw new DiaryException("非法参数");
        }
        //获取分页分享
        Page<Topic> topics = topicService.getTopicsPageOrderByFavourite(page, TOPIC_PAGE_SIZE);
        map.put("topics", topics);
        if (UserUtils.checkLogin()) {
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
        return PageConstant.HOME;
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
            throw new DiaryException(WRONG_FORMAT);
        }
        //    获取达人的分页信息
        Pagination<User> topPeoples = userService.getTopPeople(page, PEOPLE_PAGE_SIZE);
        map.put("topPeoples", topPeoples);
        if (UserUtils.checkLogin()) {
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
        return PageConstant.HOME;
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
        if (UserUtils.checkLogin()) {
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
        return PageConstant.HOME;
    }

    /**
     * 用户登录.
     *
     * @param map the map
     * @param session the session
     * @return 登录成功返回主页,失败返回错误页面
     */
    @RequestMapping("/sign_in")
    @ResponseBody
    public CommonResponse userLogin(Map<String, Object> map, HttpSession session, Principal principal) {
        if (principal == null) {
            return new CommonResponse(false, "用户名或密码有误");
        }
        User user = userService.getByName(principal.getName());
        map.put("user", user);
        return new CommonResponse(true, "登录成功");
    }

    /**
     * 表单回显,用于用户注册或登录.
     *
     * @param map the map
     * @return 引导用户登录界面或注册界面
     */
    @GetMapping("/login")
    public String input(User user, Map<String, Object> map) {
        return PageConstant.USER_INPUT;
    }

    @PostMapping("/sign_up")
    @ResponseBody
    public CommonResponse createUser(@Valid User user, BindingResult result) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "user: {}", user);
        if (result.hasErrors()) {
            StringBuilder message = new StringBuilder("提交信息有误:").append(PageConstant.BR);
            result.getAllErrors().stream().iterator().forEachRemaining(
                    objectError -> message.append(objectError.getDefaultMessage()).append(PageConstant.BR));
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " message : {}", message.toString());
            return new CommonResponse(false, message.toString());
        }
        user.setEmail(EncryptUtil.execEncrypt(user.getEmail()));
        if (StringUtils.hasChinese(user.getUsername())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            user.setAlias(simpleDateFormat.format(new Date()));
        }
        user.setPassword(EncryptUtil.execEncrypt(user.getPassword()));
        List<Roles> roles = new ArrayList<>();
        Roles userRole = rolesService.getByName(UserRoleMap.USER);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " userRole : {}", userRole);
        roles.add(userRole);
        user.setRoles(roles);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " user : {}", user);
        if (userService.newOrUpdate(user)) {
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " user.getId() : {}", user.getId());

            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            return new CommonResponse(true, "注册成功");
        }
        return new CommonResponse(false, "注册失败,请稍后再试");
    }

    /**
     * 注销用户;.
     *
     * @param session the session
     * @param map the map
     * @return 主页;
     */
    @GetMapping("/logOff")
    public String logOff(HttpSession session, Map<String, Object> map) {
        log.debug("注销");
        map.clear();
        session.invalidate();
        return "redirect:/home";
    }

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
    public CommonResponse validateUserName(User user, @RequestParam("username") String username) {
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
     * 滚动翻页.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @GetMapping("/scrollInPage")
    @ResponseBody
    public CommonResponse scrollInPage(@SessionAttribute(name = "scrollInPage", required = false) boolean scrollInPage,
            Map<String, Object> map) {
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
