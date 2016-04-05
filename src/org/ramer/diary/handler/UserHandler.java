package org.ramer.diary.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Notifying;
import org.ramer.diary.domain.Reply;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.exception.LinkInvalidException;
import org.ramer.diary.exception.NoPictureException;
import org.ramer.diary.exception.PasswordNotMatchException;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserExistException;
import org.ramer.diary.exception.UserNotExistException;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.exception.UsernameOrPasswordNotMatchException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.MailUtils;
import org.ramer.diary.util.Pagination;

/**
 * The Class UserHandler.
 *
 * @author ramer.
 */

/**
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class UserHandler {
  @Autowired
  private UserService userService;
  // 初始化在他人主页变量
  private boolean inOtherPage = false;
  // 主页面
  private final String HOME = PageConstant.HOME.toString();
  // 初始化在分享页面变量
  private boolean inTopicPage = false;
  // 全局出错页面
  private final String ERROR = PageConstant.ERROR.toString();
  // 全局成功页面
  private final String SUCCESS = PageConstant.SUCCESS.toString();
  //  用户表单
  private final String USERINPUT = PageConstant.USERINPUT.toString();
  //  数据格式错误信息
  private final String WRONGFORMAT = MessageConstant.WRONGFORMAT.toString();
  //  密码修改成功信息
  private final String SUCCESSCHANGEPASS = MessageConstant.SUCCESSCHANGEPASS.toString();
  //	默认成功信息
  private final String SUCCESSMESSAGE = MessageConstant.SUCCESSMESSAGE.toString();
  //分享页面大小
  @Value("#{diaryProperties['topic.page.size']}")
  private int TOPICPAGESIZE;
  //达人页面大小
  @Value("#{diaryProperties['topPeople.page.size']}")
  private int PEOPLEPAGESIZE;

  /**
   * 主页.
   *
   * @param pageNum 页号
   * @param map the map
   * @param session the session
   * @return 引导到主页
   */
  @RequestMapping("/home")
  public String home(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
      Map<String, Object> map, HttpSession session) {
    System.out.println("主页");
    System.out.println("pagesize = " + TOPICPAGESIZE);
    int page = 1;
    inOtherPage = false;
    inTopicPage = false;
    try {
      page = Integer.parseInt(pageNum);
      if (page < 1) {
        page = 1;
      }
    } catch (Exception e) {
      throw new IllegalAccessException("非法参数");
    }
    //获取分页分享
    Page<Topic> topics = userService.getTopicsPage(page, TOPICPAGESIZE);
    map.put("topics", topics);
    if (checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = userService.getNotifiedNumber(user);
      int topicNumber = userService.getTopicNumber(user);
      int followedNumber = userService.getFollowedNumber(user);
      System.out.println(
          "获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
      map.put("notifiedNumber", notifiedNumber);
      map.put("topicNumber", topicNumber);
      map.put("followedNumber", followedNumber);
    }
    //清除访问的临时用户信息
    map.remove("other");
    map.remove("topic");
    session.removeAttribute("details");
    //  标识为显示分享分类
    session.setAttribute("showTopic", "true");
    //  取消标识为达人分类
    session.setAttribute("showTopPeople", "false");
    //  取消标识为热门城市分类
    session.setAttribute("showPopularCity", "false");
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
  @RequestMapping("/home/orderbyUpCounts")
  public String homeTopicOrderByUpcounts(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
      Map<String, Object> map, HttpSession session) {
    System.out.println("热门主页");

    int page = 1;
    inOtherPage = false;
    inTopicPage = false;
    try {
      page = Integer.parseInt(pageNum);
      if (page < 1) {
        page = 1;
      }
    } catch (Exception e) {
      throw new IllegalAccessException("非法参数");
    }
    //获取分页分享
    Page<Topic> topics = userService.getTopicsPageOrderByFavourite(page, TOPICPAGESIZE);
    map.put("topics", topics);
    if (checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = userService.getNotifiedNumber(user);
      int topicNumber = userService.getTopicNumber(user);
      int followedNumber = userService.getFollowedNumber(user);
      System.out.println(
          "获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
      map.put("notifiedNumber", notifiedNumber);
      map.put("topicNumber", topicNumber);
      map.put("followedNumber", followedNumber);
    }

    //清除访问的临时用户信息
    map.remove("other");
    map.remove("topic");
    session.removeAttribute("details");
    //    标识为显示分享分类
    session.setAttribute("showTopic", "true");
    //  取消标识为达人分类
    session.setAttribute("showTopPeople", "false");
    //    取消标识为热门城市分类
    session.setAttribute("showPopularCity", "false");
    return HOME;
  }

  /**
   * 达人
   * @return
   */
  @RequestMapping("/home/topPeople")
  public String homeTopPeople(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
      Map<String, Object> map, HttpSession session) {
    System.out.println("达人主页");
    int page = 1;
    try {
      page = Integer.parseInt(pageNum);
      if (page < 1) {
        page = 1;
      }
    } catch (Exception e) {
      throw new IllegalAccessException(WRONGFORMAT);
    }
    //    获取达人的分页信息
    Pagination<User> topPeoples = userService.getTopPeople(page, PEOPLEPAGESIZE);
    map.put("topPeoples", topPeoples);
    if (checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = userService.getNotifiedNumber(user);
      int topicNumber = userService.getTopicNumber(user);
      int followedNumber = userService.getFollowedNumber(user);
      System.out.println(
          "获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
      map.put("notifiedNumber", notifiedNumber);
      map.put("topicNumber", topicNumber);
      map.put("followedNumber", followedNumber);
    }
    map.remove("other");
    map.remove("topic");
    session.removeAttribute("details");
    //    标识为达人分类
    session.setAttribute("showTopPeople", "true");
    //    取消标识为分享分类
    session.setAttribute("showTopic", "false");
    //    取消标识为热门城市分类
    session.setAttribute("showPopularCity", "false");
    return HOME;
  }

  /**
   * 热门城市:
   *  先获取所有城市按统计的次数排序,
   *  取得第一个城市名,并获取对应的分享
   * @param pageNum 页号
   * @param session
   * @param map
   * @return
   */
  @RequestMapping("/home/groupByCity")
  public String homeTopicGroupByCity(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
      HttpSession session, Map<String, Object> map) {
    System.out.println("热门城市主页");
    int page = 1;
    inOtherPage = false;
    inTopicPage = false;
    try {
      page = Integer.parseInt(pageNum);
      if (page < 1) {
        page = 1;
      }
    } catch (Exception e) {
      throw new IllegalAccessException("数据格式有误");
    }
    if (checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = userService.getNotifiedNumber(user);
      int topicNumber = userService.getTopicNumber(user);
      int followedNumber = userService.getFollowedNumber(user);
      System.out.println(
          "获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
      map.put("notifiedNumber", notifiedNumber);
      map.put("topicNumber", topicNumber);
      map.put("followedNumber", followedNumber);
    }
    List<String> cities = userService.getAllCities();
    String city = cities.iterator().next();
    Pagination<Topic> cityTopics = userService.getTopicsPageByCity(city, page, TOPICPAGESIZE);
    //   将所有城市写入session
    session.setAttribute("cities", cities);
    //    将第一个城市对应的分页分享写入session
    session.setAttribute("cityTopics", cityTopics);
    //    取消标识为达人分类
    session.setAttribute("showTopPeople", "false");
    //    取消标识为分享分类
    session.setAttribute("showTopic", "false");
    //    标识为热门城市分类
    session.setAttribute("showPopularCity", "true");
    return HOME;
  }

  /**
   * 通过城市获取分享
   * @param pageNum 页号
   * @param city 城市名
   * @param session
   * @param map
   * @return
   */
  @RequestMapping("/home/groupByCity/{city}")
  public String homeTopicGroupByCityString(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
      @PathVariable("city") String city, HttpSession session, Map<String, Object> map) {
    List<String> cities = userService.getAllCities();
    int page;
    inOtherPage = false;
    inTopicPage = false;
    try {
      page = Integer.parseInt(pageNum);
      if (page < 1) {
        page = 1;
      }
    } catch (Exception e) {
      throw new IllegalAccessException(WRONGFORMAT);
    }
    try {
      city = new String(city.getBytes("ISO-8859-1"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if (checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = userService.getNotifiedNumber(user);
      int topicNumber = userService.getTopicNumber(user);
      int followedNumber = userService.getFollowedNumber(user);
      System.out.println(
          "获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
      map.put("notifiedNumber", notifiedNumber);
      map.put("topicNumber", topicNumber);
      map.put("followedNumber", followedNumber);
    }
    Pagination<Topic> cityTopics = userService.getTopicsPageByCity(city, page, TOPICPAGESIZE);
    //   将所有城市写入session
    session.setAttribute("cities", cities);
    //    将第一个城市对应的分页分享写入session
    session.setAttribute("cityTopics", cityTopics);
    //    取消标识为达人分类
    session.setAttribute("showTopPeople", "false");
    //    取消标识为分享分类
    session.setAttribute("showTopic", "false");
    //    标识为热门城市分类
    session.setAttribute("showPopularCity", "true");
    return HOME;

  }

  /**
   * 表单回显,用于用户注册或登录
   * @param session the session
   * @param map the map
   * @return 引导用户登录界面或注册界面
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String input(HttpSession session, Map<String, Object> map) {
    System.out.println("表单回显,空白用户");
    map.put("user", new User());
    return USERINPUT;
  }

  /**
   * 表单回显,用于更新用户
   * @param session the session
   * @param id UID
   * @param map the map
   * @return 引导用户更新界面
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  public String input(HttpSession session, @PathVariable("id") Integer id,
      Map<String, Object> map) {
    System.out.println("表单回显,id = " + id);
    User user = (User) map.get("user");
    if (user != null && user.getId() == id) {
      map.put("user", userService.getById(user.getId()));
      return USERINPUT;
    }
    System.out.println("更新用户表单回显,非法访问");
    throw new IllegalAccessException("无法访问用户信息,登录已过期");

  }

  /**
   * 更新前获取user.
   *
   * @param id UID
   * @param map the map
   */
  @ModelAttribute
  public void getUser(@RequestParam(value = "id", required = false) Integer id,
      Map<String, Object> map) {
    if (id != null) {
      System.out.println("更新前获取user");
      System.out.println("id = " + id);
      User user = userService.getById(id);
      map.put("user", user);
    }
  }

  /**
   * 注册或更新用户信息.
   *
   * @param user 用户
   * @param file 头像文件
   * @param session the session
   * @param map the map
   * @return 如果用户名存在或执行操作错误返回错误页面.
   *      否则,更新返回个人主页,注册返回主页.
   */
  //  由于需要上传文件form 带有属性enctype="multipart/form-data",因此无法使用PUT请求
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public String newOrUpdate(User user, @RequestParam("picture") MultipartFile file,
      HttpSession session, Map<String, Object> map) {
    user.setPassword(Encrypt.execEncrypt(user.getPassword()));
    //  如果是更新,用户ID不为空
    if (userService.getByName(user.getName()) != null && user.getId() == null) {
      throw new UserExistException("用户名已存在,注册失败");
    }
    //包含中文名称的用户,先设置别名
    if (hasChinese(user.getName())) {
      System.out.println("用户名包含中文");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
      String alias = simpleDateFormat.format(new Date());
      user.setAlias(alias);
    }
    //先保存图片
    if (!file.isEmpty()) {
      System.out.println("保存图片");
      String pictureUrl;
      try {
        pictureUrl = saveFile(file, session, true, hasChinese(user.getName()));
        user.setHead(pictureUrl);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Integer id = user.getId();
    if (userService.newOrUpdate(user).getId() > 0) {
      user = userService.login(user);
      if (user.getId() == null) {
        throw new SystemWrongException("系统出错了,操作被取消,请返回重新操作");
      }
      map.put("user", user);
      //更新用户应返回到个人主页
      if (id != null) {
        return "redirect:/user/personal";
      }
      //注册用户返回到主页
      return "redirect:/home";
    }
    throw new SystemWrongException("系统出错了,操作被取消,请返回重新操作");
  }

  /**
   * 用户登录.
   *
   * @param user the user
   * @param map the map
   * @param session the session
   * @return 登录成功返回主页,失败返回错误页面
   */
  @RequestMapping(value = "/user/login")
  public String userLogin(User user, Map<String, Object> map, HttpSession session) {
    System.out.println("登录");
    String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    if (user.getName().matches(regex)) {
      System.out.println("通过邮箱登录");
      user.setEmail(Encrypt.execEncrypt(user.getName()));
      user.setName(null);
    }
    user.setPassword(Encrypt.execEncrypt(user.getPassword()));
    User user2 = userService.login(user);
    if (user2.getId() != null) {
      map.put("user", user2);
      return "redirect:/home";
    }
    throw new UsernameOrPasswordNotMatchException("登录失败,用户名或密码错误");
  }

  /**
   * 验证用户名.
   * 如果用户名存在,写入true,否者写入false
   *
   * @param user 当更新时,表示更新用户
   * @param username 当前用户输入或自动填充的用户名
   * @param response the response
   * @param session the session
   * @param map the map
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/validateUserName")
  public void validateUserName(User user, @RequestParam("username") String username,
      HttpServletResponse response, HttpSession session, Map<String, Object> map)
          throws IOException {
    System.out.println("验证用户名");
    response.setCharacterEncoding("UTF-8");
    if (username == null || username.trim().equals("")) {
      return;
    }
    //    id存在,用户更新
    if (user.getId() != null && user.getId() > 0) {
      System.out.println("用户更新: name  : " + user.getName());
      if (user.getName().equals(username)) {
        response.getWriter().write("false");
      }
    }
    if (userService.getByName(username) == null) {
      response.getWriter().write("false");
      return;
    } else {
      response.getWriter().write("true");
      return;
    }
  }

  /**
   * 验证邮箱是否可用
   * @param emailString 邮箱字符串
   * @param response
   * @param map
   * @throws IOException
   */
  @RequestMapping(value = "/user/validateEmail", method = RequestMethod.POST)
  public void validateEmail(@RequestParam("email") String emailString, HttpServletResponse response,
      Map<String, Object> map) throws IOException {
    String result = "";
    response.setCharacterEncoding("UTF-8");
    if (emailString == null || emailString.trim().equals("")) {
      return;
    }
    if (!MailUtils.isEmail(emailString)) {
      result = "<img class='valid' src='../pictures/wrong.png' weight='10px' height='10px'>";
      response.getWriter().write(result);
      return;
    }

    if (userService.getByEmail(emailString) == null) {
      result = "<img class='valid' src='../pictures/right.png' weight='10px' height='10px'>";
      response.getWriter().write(result);
      return;
    }
    System.out.println("邮箱已存在");
    result = "<img class='valid' src='../pictures/wrong.png' weight='10px' height='10px'>";
    response.getWriter().write(result);
  }

  /**
   * 注销用户.
   *
   * @param session the session
   * @param request the request
   * @param response the response
   * @param map the map
   * @return 主页
   */
  @RequestMapping("/logOff")
  public String logOff(HttpSession session, HttpServletRequest request,
      HttpServletResponse response, Map<String, Object> map) {
    System.out.println("注销");
    map.clear();
    session.invalidate();
    //	try {
    //	 request.getRequestDispatcher("/home").forward(request, response);
    //	} catch (Exception e) {
    //	 e.printStackTrace();
    //	}
    return "redirect:/home";
  }

  /**
   * 用户发表日记.
   *
   * @param content 日记文本
   * @param personal the personal
   * @param file the file
   * @param session the session
   * @return 在主页发表分享返回主页,在个人页面发表分享返回个人页面
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/publish")
  public String publish(@RequestParam("content") String content, @RequestParam("city") String city,
      @RequestParam(value = "personal", required = false, defaultValue = "") String personal,
      @RequestParam("picture") MultipartFile file, HttpSession session) throws IOException {
    User user = (User) session.getAttribute("user");
    System.out.println("发表日记: \n\t用户名: " + user.getName());
    Topic topic = new Topic();
    //	当用户上传文件时保存文件
    if (file.isEmpty()) {
      System.out.println("未选择图片");
      throw new NoPictureException("请选择一张图片");
    }
    System.out.println("保存图片");
    String pictureUrl = saveFile(file, session, false, hasChinese(user.getName()));
    topic.setPicture(pictureUrl);
    topic.setContent(content);
    topic.setDate(new Date());
    topic.setUser(user);
    topic.setUpCounts(0);
    topic.setCity(city);
    //保存用户经历
    topic = userService.publish(topic);
    //    获取所有关注'我'的人
    List<User> followUsers = userService.getFollowUser(user);
    //通知关注用户消息
    String message = "<a href='/" + session.getServletContext().getServletContextName()
        + "/user/topic/" + topic.getId() + "'>您关注的 " + user.getName() + " 分享了一个新的旅行日记 </a>";
    for (User followUser : followUsers) {
      System.out.println("通知用户: " + followUser.getId());
      userService.notifyFollowUser(user, followUser, message);
    }
    if (personal.equals("true")) {
      return "redirect:/user/personal";
    } else {
      return "redirect:/home";
    }
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
  @RequestMapping("/user/update")
  public String editHead(User user, @RequestParam("picture") MultipartFile file,
      HttpSession session) throws IOException {
    System.out.println("更新用户头像");
    if (!file.isEmpty()) {
      System.out.println("保存图片");
      String pictureUrl = saveFile(file, session, true, hasChinese(user.getName()));
      user.setHead(pictureUrl);
    }
    userService.updateHead(user);
    return "redirect:/user/personal";
  }

  /**
   * 个人中心.
   *
   * @param user 用户
   * @param map the map
   * @param session the session
   * @return 如果用户已登录返回个人主页,否则返回错误页面
   */
  @RequestMapping("/user/personal")
  public String personalMiddle(User user, Map<String, Object> map, HttpSession session) {
    inOtherPage = false;
    inTopicPage = false;
    if (!checkLogin(session)) {
      throw new UserNotLoginException("您的登录已过期,请重新登录");
    }
    // 避免懒加载异常,重新获取user
    user = userService.getById(user.getId());
    System.out.println("个人中心");
    String hasCheck = "false";
    Set<Notifying> notifyings = userService.getNotifyings(user, hasCheck);
    user.setNotifyings(notifyings);
    System.out.println(" 用户 " + user.getId() + " 收到 " + notifyings.size() + "	条消息");
    map.put("user", user);
    map.put("notifyingCount", notifyings.size());
    return "personal";
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
    inOtherPage = false;
    inTopicPage = true;
    //  查看分享
    System.out.println("-----查看分享-----");
    Topic topic = userService.getTopicById(topic_id);
    User user = (User) session.getAttribute("user");
    if (checkLogin(session)) {
      System.out.println("已登录,写入信息");
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
   * 访问他人主页.
   *
   * @param id UID
   * @param map the map
   * @param session the session
   * @return 引导到他人主页
   */
  @RequestMapping("/user/personal/{id}")
  public String visitOtherPage(@PathVariable("id") Integer id, Map<String, Object> map,
      HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user != null && id == user.getId()) {
      //获取点赞信息
      System.out.println("访问个人空间");
      List<Integer> praises = praiseToList(user, user, session);
      session.setAttribute("praises", praises);
      map.put("user", userService.getById(id));
      return "redirect:/user/personal";
    }
    inOtherPage = true;
    User other = userService.getById(id);
    if (other == null) {
      throw new UserNotExistException("您访问的用户不存在");
    }
    if (checkLogin(session)) {
      System.out.println("已登录,写入信息");
      //获取收藏信息
      List<Integer> favourites = favouriteToList(user, other, session);
      //获取点赞信息
      List<Integer> praises = praiseToList(user, other, session);
      System.out.println("访问" + other.getName() + "的空间");
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
   * 判断是否已关注该用户
   * @param user
   * @param followedUser
   * @return 已关注返回true
   */
  private boolean isFollowed(User user, User followedUser) {
    boolean flag = userService.isFollowed(user, followedUser);
    return (flag == true) ? true : false;
  }

  /**
   * 将指定用户收藏信息存储到list中
   * @param user 用户
   * @param topic 该用户的分享
   * @return list中存储的是当前用户收藏的所有,当前被浏览用户,的分享的id.
   */
  private List<Integer> favouriteToList(User user, User other, HttpSession session) {
    if (!checkLogin(session)) {
      return new ArrayList<>();
    }
    List<Integer> list = userService.getFavouriteTopicIds(user, other);
    System.out.println("已收藏分享id : ");
    for (Integer integer : list) {
      System.out.println("\t" + integer);
    }
    return list;
  }

  private List<Integer> praiseToList(User user, User other, HttpSession session) {
    if (!checkLogin(session)) {
      return new ArrayList<>();
    }
    List<Integer> list = userService.getPraiseTopicIds(user, other);
    System.out.println("已点赞分享id : ");
    for (Integer integer : list) {
      System.out.println("\t" + integer);
    }
    return list;
  }

  /**
   * 添加关注.
   *
   * @param topic 用户查看的分享
   * @param map the map
   * @param response the response
   * @param session the session
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/topic/follow")
  public void follow(User user, Topic topic, Map<String, Object> map, HttpServletResponse response,
      HttpSession session) throws IOException {
    System.out.println("添加关注");
    response.setCharacterEncoding("UTF-8");
    if (!checkLogin(session)) {
      System.out.println("用户未登录");
      response.getWriter().write("主人说没登录不能关注哒");
      return;
    }
    User followedUser = new User();
    followedUser = (topic.getId() == null) ? (User) session.getAttribute("other") : topic.getUser();
    System.out.println("被关注用户: " + followedUser.getName());
    // 虽然在访问他人的主页时,topic没有显示写入到map中,但是在页面EL和foreach迭代输出的时候,产生了topic,
    //springmvc会将此topic封装,因此这里的topic是最后一个被迭代的topic对象
    boolean flag = userService.follow(user, followedUser);
    if (flag) {
      response.getWriter().write("关注成功 !");
      return;
    }
    response.getWriter().write("你已经关注过他/她了 !");
  }

  /**
   * 取消关注用户
   * @param topic  查看的分享
   * @param map the map
   * @param response jsp内置对象
   * @param session  the session
   * @throws IOException 写入信息失败抛出异常
   */
  @RequestMapping("/user/topic/notFollow")
  public void notFollow(Topic topic, Map<String, Object> map, HttpServletResponse response,
      HttpSession session) throws IOException {
    System.out.println("取消关注");
    response.setCharacterEncoding("UTF-8");
    if (!checkLogin(session)) {
      System.out.println("用户未登录");
      response.getWriter().write("请先登录再继续操作");
      return;
    }
    User user = (User) session.getAttribute("user");
    User followedUser = new User();
    followedUser = (topic == null) ? (User) session.getAttribute("other") : topic.getUser();
    System.out.println("被关注用户: " + followedUser.getName());
    // 虽然在访问他人的主页时,topic没有显示写入到map中,但是在页面EL和foreach迭代输出的时候,产生了topic,
    //springmvc会将此topic封装,因此这里的topic是最后一个被迭代的topic对象
    boolean flag = userService.notFollow(user, followedUser);
    if (flag) {
      response.getWriter().write("取消关注成功 !");
      return;
    }
    response.getWriter().write("取消失败 !");
  }

  /**
   * 收藏.
   * @param topic_id 分享id
   * @param user 用户
   * @param response the response
   * @param session the session
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/topic/favourite/{topic_id}")
  public void favourite(@PathVariable("topic_id") Integer topic_id, User user,
      HttpServletResponse response, HttpSession session) throws IOException {
    response.setCharacterEncoding("UTF-8");
    if (!checkLogin(session)) {
      response.getWriter().write("麻麻说没登录不能收藏哒 !");
      return;
    }
    Topic topic = new Topic();
    topic.setId(topic_id);
    boolean flag = userService.favourite(user, topic);
    if (flag) {
      response.getWriter().write("收藏成功 !");
      return;
    }
    response.getWriter().write("你已经收藏过了哒 !");
  }

  /**
   * @param topic_id 分享ID
   * @param user 登录用户
   * @param response JSP内置对象
   * @param session JSP内置对象
   * @throws IOException 写入信息失败抛出IO异常
   */
  @RequestMapping("/user/topic/notFavourite/{topic_id}")
  public void notFavourite(@PathVariable("topic_id") Integer topic_id, User user,
      HttpServletResponse response, HttpSession session) throws IOException {
    response.setCharacterEncoding("UTF-8");
    Topic topic = new Topic();
    topic.setId(topic_id);
    boolean flag = userService.notFavourite(user, topic);
    if (flag) {
      response.getWriter().write("取消收藏成功 !");
      return;
    }
    response.getWriter().write("你没收藏哒 !");
  }

  /**
   * 用户点赞.
   *
   * @param topic_id 分享UID
   * @param map the map
   * @param response the response
   * @param request the request
   * @param session the session
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/topic/praise/{topic_id}")
  public void praise(@PathVariable("topic_id") Integer topic_id, Map<String, Object> map,
      HttpServletResponse response, HttpServletRequest request, HttpSession session)
          throws IOException {
    response.setCharacterEncoding("utf-8");
    User user = (User) map.get("user");
    if (!checkLogin(session)) {
      response.getWriter().write("麻麻说没登录不能点赞哒 !");
      return;
    }
    System.out.println("-----用户点赞-----");
    Topic topic = new Topic();
    topic.setId(topic_id);

    if (!userService.praise(user, topic)) {
      System.out.println("-----重复点赞-----");
      response.getWriter().write("你已经赞过了哒 !");
      return;
    }
    response.getWriter().write("恭喜你骚年,她已经悄悄收下你的赞 !");
  }

  /**
   * @param topic_id 分享ID
   * @param user 登录用户
   * @param map the map
   * @param response JSP内置对象
   * @param request JSP内置对象
   * @param session JSP内置对象
   * @throws IOException 写入信息失败抛出IO异常
   */
  @RequestMapping("/user/topic/notPraise/{topic_id}")
  public void notPraise(@PathVariable("topic_id") Integer topic_id, User user,
      Map<String, Object> map, HttpServletResponse response, HttpServletRequest request,
      HttpSession session) throws IOException {
    System.out.println("取消点赞");
    response.setCharacterEncoding("utf-8");
    if (!checkLogin(session)) {
      throw new UserNotLoginException("没登录的哦");
    }
    Topic topic = new Topic();
    topic.setId(topic_id);
    if (!userService.notPraise(topic, user)) {
      System.out.println("重复取消点赞");
      response.getWriter().write("重复取消点赞 !");
    }
    System.out.println("取消点赞成功");
    response.getWriter().write("取消点赞成功 !");
    return;
  }

  /**
   * 删除分享.
   *
   * @param user 用户
   * @param topic_id 分享UID
   * @param map the map
   * @param session the session
   * @return 重定向到个人页面,或返回错误页面
   */
  @RequestMapping("/user/topic/deleteTopic/{topic_id}")
  public String deleteTopic(User user, @PathVariable("topic_id") Integer topic_id,
      Map<String, Object> map, HttpSession session) {
    System.out.println("-----删除分享-----");
    //如果访问的临时用户存在,说明当前用户在他人的分享主页
    //用户将无法执行删除
    if (map.keySet().contains("other")) {
      throw new IllegalAccessException("你没有删除该条分享的权限");
    }
    Topic topic = userService.getTopicById(topic_id);
    userService.deleteTopic(topic);
    boolean flag = deleteFile(topic, session, hasChinese(user.getName()));
    System.out.println("-----删除图片 : " + flag + "-----");
    return "redirect:/user/personal";
  }

  /**
   * 发表用户评论.
   *
   * @param topic_id 分享UID
   * @param content 分享内容
   * @param map the map
   * @param session the session
   * @param response the response
   * @return 返回到他人主页或某个分享页面
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/topic/comment/{topic_id}")
  public String comment(@PathVariable("topic_id") Integer topic_id,
      @RequestParam("content") String content, Map<String, Object> map, HttpSession session,
      HttpServletResponse response) throws IOException {
    System.out.println("用户评论");
    if (!checkLogin(session)) {
      throw new UserNotLoginException("要先登录,才能评论哦 !");
    }
    System.out.println("-----用户评论-----");
    System.out.println(content);
    User user = (User) map.get("user");
    Topic topic = new Topic();
    Comment comment = new Comment();
    comment.setContent(content);
    topic.setId(topic_id);
    //防止用户恶意修改页面userid导致程序崩溃
    comment.setUser(user);
    comment.setTopic(topic);
    comment.setDate(new Date());
    userService.comment(comment);
    //在他人主页
    if (inOtherPage) {
      User other = (User) session.getAttribute("other");
      System.out.println("在他人主页评论");
      return "redirect:/user/personal/" + other.getId();
    }
    // 在某个分享页面
    if (inTopicPage) {
      System.out.println("在分享页面评论");
      return "redirect:/user/topic/" + topic_id;
    }
    System.out.println("在个人主页评论");
    return "redirect:/user/personal";
  }

  /**
   * 删除某个评论.
   *
   * @param comment_id 评论UID
   * @param topic_id 分享UID
   * @param map the map
   * @param session the session
   * @return 重定向到个人页面
   */
  @RequestMapping("/user/topic/comment/delete/{comment_id}")
  public String deleteComment(@PathVariable("comment_id") String comment_id,
      @RequestParam("topic") String topic_id, Map<String, Object> map, HttpSession session) {
    System.out.println("-----删除某个评论-----");
    //如果在他人页面,代码属于人为构造
    //用户将无法执行删除
    if (inOtherPage) {
      throw new IllegalAccessException("你没有删除该条评论的权限");
    }
    Comment comment = new Comment();
    Topic topic = new Topic();
    //防止用户非法传入topic参数
    comment.setId(Integer.parseInt(comment_id));
    topic.setId(Integer.parseInt(topic_id));
    // 如果在分享页面,判断是否为本人的分享,非本人的分享将无法删除
    if (inTopicPage
        && !userService.getTopicByUserIdAndTopicId(topic.getId(), (User) map.get("user"))) {
      throw new IllegalAccessException("你没有删除该条评论的权限");
    }
    comment.setTopic(topic);
    if (!userService.deleteComment(comment)) {
      throw new SystemWrongException();
    }
    return "redirect:/user/personal";
  }

  /**
   * 回复评论.
   *
   * @param id 评论ID
   * @param content 回复内容
   * @param user 登录用户
   * @param session JSP内置对象
   * @param response JSP内置对象
   * @param map the map
   * @return 返回当前页面
   * @throws IOException 写入信息失败
   */
  @RequestMapping("/user/topic/comment/reply/{comment_id}")
  public String replyComment(@PathVariable("comment_id") String id,
      @RequestParam("content") String content, User user, HttpSession session,
      HttpServletResponse response, Map<String, Object> map) throws IOException {
    if (!checkLogin(session)) {
      throw new UserNotLoginException("您还未登录,或登录已过期,请登录");
    }
    Integer comment_id = 0;
    try {
      comment_id = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      System.out.println("格式化异常");
    }
    System.out.println(comment_id);
    Reply reply = new Reply();
    reply.setContent(content);
    reply.setDate(new Date());
    reply.setUser(user);
    Comment comment = new Comment();
    comment.setId(comment_id);
    reply.setComment(comment);
    response.setCharacterEncoding("utf-8");
    if (!userService.replyComment(reply)) {
      return ERROR;
    }
    if (inOtherPage) {
      User other = (User) session.getAttribute("other");
      System.out.println("在他人主页评论");
      return "redirect:/user/personal/" + other.getId();
    }
    // 在某个分享页面
    if (inTopicPage) {
      System.out.println("在分享页面评论");
      Topic topic = (Topic) session.getAttribute("topic");
      return "redirect:/user/topic/" + topic.getId();
    }
    System.out.println("在个人主页评论");
    return "redirect:/user/personal";

  }

  /**
   * 删除一个回复
   *
   * @param id the id
   * @param user the user
   * @param map the map
   * @param session the session
   * @return the string
   */
  @RequestMapping("/user/topic/reply/delete/{reply_id}")
  public String deleteReply(@PathVariable("reply_id") String id, User user, Map<String, Object> map,
      HttpSession session) {
    System.out.println("删除回复");
    Integer reply_id = 0;
    try {
      reply_id = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      System.out.println("数据格式错误");
      throw new IllegalAccessException("数据格式有误");
    }
    if (reply_id != 0) {
      if (userService.deleteReply(reply_id)) {
        if (inOtherPage) {
          User other = (User) session.getAttribute("other");
          System.out.println("在他人主页评论");
          return "redirect:/user/personal/" + other.getId();
        }
        // 在某个分享页面
        if (inTopicPage) {
          System.out.println("在分享页面评论");
          Topic topic = (Topic) session.getAttribute("topic");
          return "redirect:/user/topic/" + topic.getId();
        }
        System.out.println("在个人主页评论");
        return "redirect:/user/personal";
      }
    }
    return ERROR;
  }

  /**
   * 发送私信.
   *
   * @param user 用户
   * @param content 私信内容
   * @param map the map
   * @param response the response
   * @param session the session
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/personal/sendPrivMess")
  public void sendPrivMess(User user, @RequestParam("content") String content,
      Map<String, Object> map, HttpServletResponse response, HttpSession session)
          throws IOException {
    System.out.println("发送私信");
    response.setCharacterEncoding("utf-8");
    if (!checkLogin(session)) {
      response.getWriter().write("需要先登录才能说悄悄话哦");
      System.out.println("未登录");
      return;
    }
    if (content.trim() == null || content.trim() == "") {
      System.out.println("消息为空");
      response.getWriter().write("消息不能为空");
      return;
    }
    User notifiedUser = new User();
    notifiedUser = (User) session.getAttribute("other");
    System.out.println("被通知用户 = " + notifiedUser);
    Notifying notifying = new Notifying();
    notifying.setUser(user);
    notifying.setNotifiedUser(notifiedUser);
    notifying.setContent(content);
    notifying.setDate(new Date());
    notifying.setHasCheck("false");
    boolean flag = userService.sendPrivMess(notifying);
    response.setCharacterEncoding("utf-8");
    if (!flag) {
      System.out.println("消息发送失败");
      response.getWriter().write("系统正在打麻将,请稍后再试");
      return;
    }
    System.out.println("消息发送成功");
    response.getWriter().write("消息发送成功");
  }

  /**
   * 用户阅读消息.
   *
   * @param notifyId 信息UID
   * @param map the map
   * @param session the session
   * @return 重定向到个人主页
   */
  @RequestMapping("/user/personal/readPrivMess")
  public String readPrivMess(@RequestParam("notifyId") String notifyId, Map<String, Object> map,
      HttpSession session) {
    System.out.println("读取私信 : " + notifyId);
    Notifying notifying = new Notifying();
    Integer notify_id = -1;
    try {
      notify_id = Integer.parseInt(notifyId);
    } catch (Exception e) {
      throw new IllegalAccessException("数据格式有误");
    }
    if (notify_id > 0) {
      notifying.setId(notify_id);
      notifying = userService.getNotifyingById(notifying);
      notifying.setHasCheck("true");
      boolean flag = userService.updateNotifying(notifying);
      if (!flag) {
        throw new SystemWrongException();
      }
    } else {
      throw new IllegalAccessException("数据格式有误");
    }
    return "redirect:/user/personal";
  }

  /**
   * 重定向到修改密码页面.
   *
   * @param map the map
   * @param session the session
   * @return 引导到修改密码页面
   */
  @RequestMapping(value = "/user/forwardModifyPassword", method = RequestMethod.GET)
  public String forwardModifyPassword(Map<String, Object> map, HttpSession session) {
    if (!checkLogin(session)) {
      throw new UserNotLoginException("您还未登录或登录已过期");
    }
    System.out.println("引导到修改用户密码页面");
    return "modifyPass";
  }

  /**
   * 修改密码.
   *
   * @param oldPassword 原始密码
   * @param newPassword 新密码
   * @param user 用户
   * @param map the map
   * @param response the response
   * @param session the session
   * @return 密码修改成功: 返回个人主页,失败: 返回密码修改页面
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/modifyPassword")
  public String modifyPassword(@RequestParam("oldPassword") String oldPassword,
      @RequestParam("newPassword") String newPassword, User user, Map<String, Object> map,
      HttpServletResponse response, HttpSession session) throws IOException {
    if (!Encrypt.execEncrypt(oldPassword).equals(user.getPassword())) {
      System.out.println("原始密码错误");
      session.setAttribute("error_modifyPass", "原始密码错误");
      return "redirect:/user/forwardModifyPassword";
    }
    if (oldPassword.equals(newPassword)) {
      System.out.println("密码未改变");
      session.setAttribute("error_modifyPass", "");
      session.setAttribute("succMessage", "密码修改成功");
      return SUCCESS;
    }
    user.setPassword(Encrypt.execEncrypt(newPassword));
    user = userService.newOrUpdate(user);
    if (user == null) {
      throw new SystemWrongException();
    }
    session.setAttribute("error_modifyPass", "");
    session.setAttribute("succMessage", "密码修改成功");
    return SUCCESS;
  }

  /**
   * 重定向到忘记密码页面.
   * @param email 用户绑定的邮箱
   * @param map the map
   * @param session JSP内置对象
   * @return 引导到忘记密码页面
   */
  @RequestMapping(value = "/user/forwardForgetPassword", method = RequestMethod.GET)
  public String forwardForgetPassword(
      @RequestParam(value = "email", required = false, defaultValue = "") String email,
      Map<String, Object> map, HttpSession session) {
    System.out.println("引导到忘记用户密码页面");
    if (!email.equals("") && email != null) {
      map.put("email", "email");
    }
    return "forgetPass";
  }

  /**
   * 发送邮件.
   *
   * @param email 未加密的邮箱地址
   * @param map the map
   * @param session the session
   * @param response JSP内置对象
   * @throws IOException 写入信息失败抛出IO异常
   */
  @RequestMapping("/user/forgetPass/sendMail")
  public void sendMailToResetPass(@RequestParam("email") String email, Map<String, Object> map,
      HttpSession session, HttpServletResponse response) throws IOException {
    response.setCharacterEncoding("utf-8");
    if (email.trim() == null || email.trim().equals("")) {
      response.getWriter().write("臣妾还不知道发到哪儿呐");
      return;
    }
    if (!MailUtils.isEmail(email)) {
      response.getWriter().write("您输入的不是邮箱哒 ^o^||");
      return;
    }
    String encodedEmail = Encrypt.execEncrypt(email);
    User user = userService.getByEmail(encodedEmail);
    //    发送邮件之前判断是否存在,防止用户而已发送邮件
    if (user == null) {
      response.getWriter().write("您输入的邮箱未注册哟");
      return;
    }
    String servletName = session.getServletContext().getServletContextName();
    String content = "<h3>请点击下面的链接继续重置密码,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/"
        + servletName + "/user/forwardForgetPassword?email=" + encodedEmail
        + "'>http://localhost:8080/" + servletName + "/user/forgetPassword/" + email + "</a>";
    String top = "来自飞行日记的重置密码邮件";
    MailUtils.sendMail(email, top, content);
    Calendar calendar = Calendar.getInstance();
    //		时间是五分钟之后
    calendar.add(Calendar.MINUTE, 5);
    String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
    user.setExpireTime(expireTime);
    userService.newOrUpdate(user);
    response.getWriter().write("邮件发送成功");
  }

  /**
   * 忘记密码.
   *
   * @param email 已加密邮箱
   * @param password 新密码
   * @param repassword 密码重复
   * @param map the map
   * @param session the session
   * @return 密码修改成功: 返回个人主页,失败: 返回密码修改页面
   */
  @RequestMapping("/user/forgetPassword")
  public String forgetPassword(@RequestParam("email") String email,
      @RequestParam("password") String password, @RequestParam("repassword") String repassword,
      Map<String, Object> map, HttpSession session) {

    System.out.println("忘记密码,重置密码");
    User user = userService.getByEmail(email);
    //    获取当前时间,并格式化
    String expireTime = new SimpleDateFormat("yyMMddhhmmss")
        .format(Calendar.getInstance().getTime());
    if (expireTime.compareTo(user.getExpireTime()) > 0) {
      System.out.println("链接已失效");
      throw new LinkInvalidException();
    }
    if (!password.equals(repassword)) {
      throw new PasswordNotMatchException();
    }
    user.setPassword(Encrypt.execEncrypt(password));
    if (userService.newOrUpdate(user) == null) {
      throw new SystemWrongException();
    } else {
      execSuccess(session, SUCCESSCHANGEPASS);
      return SUCCESS;
    }
  }

  /**
   * 定向到修改邮箱页面.
   * @param session JSP内置对象
   * @return 修改邮箱页面
   */
  @RequestMapping("/user/forwardModifyEmail")
  public String forwardModifyEmail(HttpSession session) {
    if (!checkLogin(session)) {
      throw new UserNotLoginException("您的登录已过期,请重新登录");
    }
    return "modifyEmail";
  }

  /**
   * 发送邮件,更改邮箱.
   *
   * @param newEmail 新邮箱
   * @param user the user
   * @param map the map
   * @param session the session
   * @param response the response
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @RequestMapping("/user/modifyEmail/sendMail")
  public void sendEmailToModifyEmail(@RequestParam("newEmail") String newEmail, User user,
      Map<String, Object> map, HttpSession session, HttpServletResponse response)
          throws IOException {

    System.out.println("发送邮件,修改邮箱");
    response.setCharacterEncoding("utf-8");
    if (newEmail.trim() == null || newEmail.trim().equals("")) {
      response.getWriter().write("臣妾还不知道发到哪儿呐");
      return;
    }
    if (!MailUtils.isEmail(newEmail)) {
      response.getWriter().write("您输入的不是邮箱哒 ^o^||");
      return;
    }
    Calendar calendar = Calendar.getInstance();
    //    时间是五分钟之后
    calendar.add(Calendar.MINUTE, 5);
    String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
    user.setExpireTime(expireTime);
    userService.newOrUpdate(user);
    String servletName = session.getServletContext().getServletContextName();
    String encodedEmail = Encrypt.execEncrypt(newEmail);
    String content = "<h3>请点击下面的链接完成邮箱更改,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/"
        + servletName + "/user/modifyEmail?email1=" + encodedEmail + "&email2=" + user.getEmail()
        + "'>http://localhost:8080/" + servletName + "/user/modifyEmail/" + newEmail + "</a>";
    String top = "来自飞行日记的更改邮箱邮件";
    MailUtils.sendMail(newEmail, top, content);
    response.getWriter().write("嗖.......... 到家啦 ^v^,查收邮件后再继续操作哦");
  }

  /**
   * 修改邮箱.
   *
   * @param email 用户原来的邮箱
   * @param newEmail 修改的新邮箱
   * @param map the map
   * @param session the session
   * @return the string
   */
  @RequestMapping("/user/modifyEmail")
  public String modifyEmail(@RequestParam("email2") String email,
      @RequestParam("email1") String newEmail, Map<String, Object> map, HttpSession session) {
    System.out.println("修改邮箱");
    String expireTime = new SimpleDateFormat("yyMMddhhmmss")
        .format(Calendar.getInstance().getTime());
    User user = userService.getByEmail(email);
    if (user == null || expireTime.compareTo(user.getExpireTime()) > 0) {
      throw new LinkInvalidException("链接失效");
    }
    user.setEmail(newEmail);
    userService.newOrUpdate(user);
    execSuccess(session, "邮箱更改成功啦,去主页溜溜吧");
    user.setExpireTime(expireTime);
    return SUCCESS;
  }

  /**
   * 重定向到成功页面
   *
   * @return 重定向到成功页面
   */
  @RequestMapping("/success")
  public String forwardSuccess() {
    return SUCCESS.substring(10);
  }

  /**
   *	重定向到错误页面
   *
   * @return 重定向到错误页面
   */
  @RequestMapping("/error")
  public String forwardError() {
    return ERROR.substring(10);
  }

  /**
   * 删除分享图片.
   *
   * @param topic 图片对应的分享
   * @param session the session
   * @param chn 是否中文
   * @return 删除成功返回true
   */
  private boolean deleteFile(Topic topic, HttpSession session, boolean chn) {
    User user = (User) session.getAttribute("user");
    String username = user.getName();
    String alias = user.getAlias();
    String picture = topic.getPicture();
    String pictureName = picture.substring(picture.lastIndexOf("\\"));
    String servletPath = session.getServletContext().getRealPath("pictures");
    System.out.println("servletPath:\n\t" + servletPath + "\npictureName:\n\t" + pictureName);
    String realPath = servletPath + "\\" + username + "\\" + pictureName;
    // 如果用户名中含有中文,路径是别名
    if (chn) {
      realPath = servletPath + "\\" + alias + "\\" + pictureName;
    }
    File file = new File(realPath);
    System.out.println("删除文件 : " + file.getAbsolutePath());
    return file.exists() ? file.delete() : true;
  }

  /**
   * 保存上传的图片.
   *
   * @param file 文件流
   * @param session httpsession
   * @param head 是否用户头像
   * @param chn 用户名中是否包含中文
   * @return 返回数据库中的图片路径
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String saveFile(MultipartFile file, HttpSession session, boolean head, boolean chn)
      throws IOException {
    User user = (User) session.getAttribute("user");
    System.out.println("用户名: " + user.getName());
    String username = user.getName();
    String alias = user.getAlias();
    File userFolder = new File(
        session.getServletContext().getRealPath("pictures") + "\\" + username);
    if (chn) {
      userFolder = new File(session.getServletContext().getRealPath("pictures") + "\\" + alias);
    }
    //	判断用户文件夹是否存在,不存在则创建
    if (!userFolder.exists()) {
      userFolder.mkdir();
    }
    //获取图片的名称
    String name = file.getOriginalFilename();
    String path = session.getServletContext().getRealPath("pictures") + "\\" + username + "\\";
    if (chn) {
      path = session.getServletContext().getRealPath("pictures") + "\\" + alias + "\\";
    }
    String suffix = name.substring(name.indexOf("."));
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    name = simpleDateFormat.format(new Date());
    String pathname = path + name + suffix;
    if (head) {
      pathname = path + username + suffix;
      if (chn) {
        pathname = path + alias + suffix;
      }
    }
    System.out.println("上传的图片信息 : \n\t" + pathname);
    File saveFile = new File(pathname);
    saveFile.createNewFile();
    InputStream inputStream = file.getInputStream();
    OutputStream outputStream = new FileOutputStream(saveFile);
    byte[] bys = new byte[inputStream.available()];
    int length = 0;
    while ((length = inputStream.read(bys)) != -1) {
      outputStream.write(bys, 0, length);
    }
    inputStream.close();
    outputStream.close();
    String pictureUrl = "pictures\\" + username + "\\" + name + suffix;
    if (chn) {
      pictureUrl = "pictures\\" + alias + "\\" + name + suffix;
    }
    if (head) {
      pictureUrl = "pictures\\" + username + "\\" + username + suffix;
      if (chn) {
        pictureUrl = "pictures\\" + alias + "\\" + alias + suffix;
      }
    }
    System.out.println("数据库中的图片路径:" + pictureUrl);
    return pictureUrl;
  }

  /**
   * 判断给定的字符串中是否包含中文: 中文是全角,这种判断并不精确.
   *
   * @param args 需要判断的字符串
   * @return true, if successful
   */
  private boolean hasChinese(String args) {
    if (args.getBytes().length != args.length()) {
      return true;
    }
    return false;
  }

  /**
   * 操作成功.
   *
   * @param session the session
   * @param succMessage 成功信息
   */
  private void execSuccess(HttpSession session, String... succMessage) {
    if (succMessage.length > 0) {
      session.setAttribute("succMessage", succMessage[0]);
      return;
    }
    session.setAttribute("succMessage", SUCCESSMESSAGE);
  }

  /**
   * 检测用户是否登录.
   *
   * @param session the session
   * @return 已登录返回true,否则返回false
   */
  public boolean checkLogin(HttpSession session) {
    System.out.println("登录检测");
    if (session.getAttribute("user") != null
        && ((User) session.getAttribute("user")).getId() != null) {
      System.out.println("\t已登录");
      return true;
    }
    System.out.println("\t未登录");
    return false;
  }

}
