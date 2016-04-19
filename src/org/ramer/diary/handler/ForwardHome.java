package org.ramer.diary.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Pagination;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 定位到主页.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class ForwardHome {
  @Autowired
  private UserService userService;
  //主页面
  private final String HOME = PageConstant.HOME.toString();
  //初始化在他人主页变量
  private boolean inOtherPage = false;
  // 初始化在分享页面变量
  private boolean inTopicPage = false;
  //数据格式错误信息
  private final String WRONGFORMAT = MessageConstant.WRONGFORMAT.toString();
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
   * @throws IOException 
   */
  @RequestMapping("/home")
  public String home(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
      Map<String, Object> map, HttpSession session) throws IOException {
    System.out.println("主页");
    System.out.println(session.getServletContext().getResource("pictures").getPath());
    System.out.println("pagesize = " + TOPICPAGESIZE);
    int page = 1;
    //    inOtherPage = false;
    //    inTopicPage = false;

    //重置标识信息
    map.put("inOtherPage", inOtherPage);
    map.put("inTopicPage", inTopicPage);
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
    if (UserUtils.checkLogin(session)) {
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

    //    inOtherPage = false;
    //    inTopicPage = false;

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
    Page<Topic> topics = userService.getTopicsPageOrderByFavourite(page, TOPICPAGESIZE);
    map.put("topics", topics);
    if (UserUtils.checkLogin(session)) {
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
    if (UserUtils.checkLogin(session)) {
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
    //    inOtherPage = false;
    //    inTopicPage = false;

    session.setAttribute("inOtherPage", false);
    session.setAttribute("inTopicPage", false);
    try {
      page = Integer.parseInt(pageNum);
      if (page < 1) {
        page = 1;
      }
    } catch (Exception e) {
      throw new IllegalAccessException("数据格式有误");
    }
    if (UserUtils.checkLogin(session)) {
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
    //    inOtherPage = false;
    //    inTopicPage = false;

    session.setAttribute("inOtherPage", false);
    session.setAttribute("inTopicPage", false);
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
    if (UserUtils.checkLogin(session)) {
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

}
