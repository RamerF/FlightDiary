package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.DiaryException;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.*;
import org.ramer.diary.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 注册或更新类
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class UserController{
    @Resource
    private UserService userService;
    @Resource
    private UserValidator userValidator;
    @Resource
    private TopicService topicService;
    @Resource
    private NotifyService notifyService;
    private static final String SUCCESS = PageConstant.SUCCESS;
    private final String SUCCESS_CHANGE_PASS = MessageConstant.SUCCESS_MESSAGE;
    //分享页面大小
    @Value("${diary.personal.topic.page.size}")
    private int TOPIC_PAGE_SIZE;

    @Value("${diary.encrypt.strength}")
    private int ENCRYPT_STRENGTH;

    @InitBinder("user")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

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
            throw new DiaryException("您的登录已过期,请重新登录");
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
        Page<Topic> topicsPage = topicService.getTopicsPageByUserId(user, page, TOPIC_PAGE_SIZE);
        map.put("topicsPage", topicsPage);
        map.put("notifyCount", notifies.size());
        map.put("user", user);
        return "personal";
    }

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
            throw new DiaryException("您还未登录或登录已过期");
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
            throw new DiaryException();
        }
        session.setAttribute("succMessage", "密码修改成功");
        return SUCCESS;
    }

    /**
     * 定向到修改邮箱页面.
     *
     * @param session JSP内置对象
     * @return 修改邮箱页面 string
     */
    @GetMapping("/user/forwardModifyEmail")
    public String forwardModifyEmail(HttpSession session) {
        if (!UserUtils.checkLogin(session)) {
            throw new DiaryException("您的登录已过期,请重新登录");
        }
        return "modify_email";
    }

    /**
     * 发送邮件,更改邮箱.
     *
     * @param newEmail 新邮箱
     * @param user     the user
     * @param session  the session
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @PostMapping("/user/modifyEmail/sendMail")
    public void sendEmailToModifyEmail(@RequestParam("newEmail") String newEmail, User user, HttpSession session,
            HttpServletResponse response) throws IOException {
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            throw new DiaryException("您的登录已过期,请重新登录");
        }
        log.debug("发送邮件,修改邮箱");
        response.setCharacterEncoding("utf-8");
        if (newEmail.trim() == null || newEmail.trim().equals("")) {
            response.getWriter().write("臣妾还不知道发到哪儿呐");
            return;
        }
        if (!MailUtils.isEmail(newEmail)) {
            response.getWriter().write("您输入的不是邮箱哒 ^o^||");
            return;
        }
        //判断邮箱在数据库中是否存在
        if (MailUtils.exist(newEmail, userService)) {
            throw new DiaryException("邮箱已存在");
        }
        Calendar calendar = Calendar.getInstance();
        //    时间是五分钟之后
        calendar.add(Calendar.MINUTE, 5);
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
        user.setExpireTime(expireTime);
        userService.newOrUpdate(user);
        String servletName = session.getServletContext().getServletContextName();
        String encodedEmail = EncryptUtil.execEncrypt(newEmail);
        String content = "<h3>请点击下面的链接完成邮箱更改,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/" + servletName
                + "/user/modifyEmail?email1=" + encodedEmail + "&email2=" + user.getEmail() + "'>http://localhost:8080/"
                + servletName + "/user/modifyEmail/" + newEmail + "</a>";
        String top = "来自旅行日记的更改邮箱邮件";
        MailUtils.sendMail(newEmail, top, content);
        response.getWriter().write("嗖.......... 到家啦 ^v^,查收邮件后再继续操作哦");
    }

    /**
     * 修改邮箱.
     *
     * @param email    用户原来的邮箱
     * @param newEmail 修改的新邮箱
     * @param session  the session
     * @return the string
     */
    @PutMapping("/user/modifyEmail")
    public String modifyEmail(@RequestParam("email2") String email, @RequestParam("email1") String newEmail,
            HttpSession session) {
        log.debug("修改邮箱");
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(Calendar.getInstance().getTime());
        User user = userService.getByEmail(email);
        if (user == null || expireTime.compareTo(user.getExpireTime()) > 0) {
            throw new DiaryException("链接已失效");
        }
        //判断邮箱是否存在
        if (MailUtils.exist(newEmail, userService)) {
            throw new DiaryException("邮箱已存在");
        }
        user.setEmail(newEmail);
        userService.newOrUpdate(user);
        UserUtils.execSuccess(session, "邮箱更改成功啦,去主页溜溜吧");
        user.setExpireTime(expireTime);
        return SUCCESS;
    }

    /**
     * 重定向到忘记密码页面.
     * @param email 用户绑定的邮箱
     * @param map the map
     * @return 引导到忘记密码页面
     */
    @GetMapping("/user/forwardForgetPassword")
    public String forwardForgetPassword(
            @RequestParam(value = "email", required = false, defaultValue = "") String email, Map<String, Object> map) {
        log.debug("引导到忘记用户密码页面");
        if (!email.equals("")) {
            map.put("email", "email");
        }
        return "forget_pass";
    }

    /**
     * 发送邮件.
     *
     * @param email 未加密的邮箱地址
     * @param session the session
     * @param response JSP内置对象
     * @throws IOException 写入信息失败抛出IO异常
     */
    @PostMapping("/user/forgetPass/sendMail")
    public void sendMailToResetPass(@RequestParam("email") String email, HttpSession session,
            HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        if (email.trim() == null || email.trim().equals("")) {
            response.getWriter().write("臣妾还不知道发到哪儿呐");
            return;
        }
        if (!MailUtils.isEmail(email)) {
            response.getWriter().write("您输入的不是邮箱哒 ^o^||");
            return;
        }
        String encodedEmail = EncryptUtil.execEncrypt(email);
        User user = userService.getByEmail(encodedEmail);
        //    发送邮件之前判断是否存在,防止用户而已发送邮件
        if (user == null) {
            response.getWriter().write("您输入的邮箱未注册哟");
            return;
        }
        log.debug("邮箱认证通过");
        String servletName = session.getServletContext().getServletContextName();
        String content = "<h3>请点击下面的链接继续重置密码,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/" + servletName
                + "/user/forwardForgetPassword?email=" + encodedEmail + "'>http://localhost:8080/" + servletName
                + "/user/forgetPassword/" + email + "</a>";
        String top = "来自旅行日记的重置密码邮件";
        MailUtils.sendMail(email, top, content);
        Calendar calendar = Calendar.getInstance();
        //		时间是五分钟之后
        calendar.add(Calendar.MINUTE, 5);
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
        user.setExpireTime(expireTime);
        userService.newOrUpdate(user);
        response.getWriter().write("嗖.......... 到家啦 ^v^,查收邮件后再继续操作哦");
    }

    /**
     * 忘记密码.
     *
     * @param email 已加密邮箱
     * @param password 新密码
     * @param repassword 密码重复
     * @param session the session
     * @return 密码修改成功: 返回个人主页,失败: 返回密码修改页面
     */
    @PutMapping("/user/forgetPassword")
    public String forgetPassword(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("repassword") String repassword, HttpSession session) {

        log.debug("忘记密码,重置密码");
        User user = userService.getByEmail(email);
        //    获取当前时间,并格式化
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(Calendar.getInstance().getTime());
        if (expireTime.compareTo(user.getExpireTime()) > 0) {
            log.debug("链接已失效");
            throw new DiaryException("链接已失效");
        }
        if (!password.equals(repassword)) {
            throw new DiaryException("密码不匹配");
        }
        user.setPassword(EncryptUtil.execEncrypt(password));
        if (!userService.newOrUpdate(user)) {
            throw new DiaryException();
        }
        UserUtils.execSuccess(session, SUCCESS_CHANGE_PASS);
        return SUCCESS;
    }

    /**
     * 更新用户头像.
     *
     * @param user 用户
     * @param file 头像文件
     * @param session the session
     * @return 返回个人主页
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @GetMapping("/user/update")
    public String editHead(User user, @RequestParam("picture") MultipartFile file, HttpSession session)
            throws IOException {
        log.debug("更新用户头像");
        if (!file.isEmpty()) {
            log.debug("保存图片");
            String pictureUrl = FileUtils.saveFile(file, session, true, StringUtils.hasChinese(user.getUsername()));
            user.setHead(pictureUrl);
        }
        userService.updateHead(user);
        return "redirect:/user/personal";
    }

    /**
     * 表单回显,用于更新用户.
     *
     * @param id UID
     * @param map the map
     * @return 引导用户更新界面
     */
    @GetMapping(value = "/user/{id}")
    public String input(@PathVariable("id") Integer id, Map<String, Object> map) {
        User user = (User) map.get("user");
        if (user != null && user.getId() == id) {
            map.put("user", userService.getById(user.getId()));
            return PageConstant.USER_INPUT;
        }
        throw new DiaryException("无法访问用户信息,登录已过期");
    }

    //  由于需要上传文件form 带有属性enctype="multipart/form-data",因此无法使用PUT请求
    @PostMapping("/user/{id}/update")
    public String update(@SessionAttribute(value = "user") @Valid User user, @RequestParam("id") Integer userId,
            @RequestParam("picture") MultipartFile file, HttpSession session, Map<String, Object> map,
            @RequestParam("checkFile") String checkFile) {
        if (!userService.getByName(user.getUsername()).getId().equals(userId)) {
            throw new DiaryException("用户名已存在,更新失败");
        }
        //如果是注册需要加密密码，而更新是不允许修改密码的
        if (user.getId() == null) {
            user.setPassword(EncryptUtil.execEncrypt(user.getPassword()));
        }
        //包含中文名称的用户,先设置别名
        if (StringUtils.hasChinese(user.getUsername())) {
            log.debug("用户名包含中文");
            user.setAlias(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        }
        //先保存图片
        if (!file.isEmpty()) {
            log.debug("保存图片");
            String pictureUrl;
            try {
                pictureUrl = FileUtils.saveFile(file, session, true, StringUtils.hasChinese(user.getUsername()));
                user.setHead(pictureUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkFile = checkFile.substring(checkFile.indexOf("/", 2));
            log.debug("checkFile: {}", checkFile);
            user.setHead(checkFile);
        }
        //    判断邮箱是否存在，如果存在说明以后未修改，不要加密
        if (userService.getByEmail(user.getEmail()) == null) {
            user.setEmail(EncryptUtil.execEncrypt(user.getEmail()));
        }
        Integer id = user.getId();
        if (userService.newOrUpdate(user)) {
            user = userService.login(user);
            if (user.getId() == null) {
                throw new DiaryException("系统出错了,操作被取消,请返回重新操作");
            }
            map.put("user", user);
            //更新用户应返回到个人主页
            if (id != null) {
                return "redirect:/user/personal";
            }
            //注册用户返回到主页
            return "redirect:/home";
        }
        throw new DiaryException("系统出错了,操作被取消,请返回重新操作");
    }

}
