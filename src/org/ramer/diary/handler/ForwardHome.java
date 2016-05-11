package org.ramer.diary.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.IllegalAccessException;
import org.ramer.diary.service.FollowService;
import org.ramer.diary.service.NotifyService;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Pagination;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 定位到主页.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", "topicCount" }, types = { User.class, Topic.class })
@Controller
public class ForwardHome {
  @Autowired
  private UserService userService;
  @Autowired
  private TopicService topicService;
  @Autowired
  private NotifyService notifyService;
  @Autowired
  private FollowService followService;

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
        page = oldTopics.getTotalPages();
      }
    } catch (Exception e) {
      page = 1;
    }

    //获取分页分享
    Page<Topic> topics = topicService.getTopicsPage(page, TOPICPAGESIZE);
    //记录最新的topicid，用于判断是否有新动态
    map.put("topicCount", topicService.getCount());
    map.put("topics", topics);
    if (UserUtils.checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = notifyService.getNotifiedNumber(user);
      int topicNumber = topicService.getTopicNumber(user);
      int followedNumber = followService.getFollowedNumber(user);
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
    //  取消标识为热门标签分类
    session.setAttribute("showPopularTags", "false");
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
    Page<Topic> topics = topicService.getTopicsPageOrderByFavourite(page, TOPICPAGESIZE);
    map.put("topics", topics);
    if (UserUtils.checkLogin(session)) {
      User user = (User) session.getAttribute("user");
      //获取用户统计数据
      int notifiedNumber = notifyService.getNotifiedNumber(user);
      int topicNumber = topicService.getTopicNumber(user);
      int followedNumber = followService.getFollowedNumber(user);
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
    //    取消标识为热门标签分类
    session.setAttribute("showPopularTags", "false");
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
      int notifiedNumber = notifyService.getNotifiedNumber(user);
      int topicNumber = topicService.getTopicNumber(user);
      int followedNumber = followService.getFollowedNumber(user);
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
    //    取消标识为热门标签分类
    session.setAttribute("showPopularTags", "false");
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
      @RequestParam(value = "tag", required = false, defaultValue = "default") String tag,
      HttpSession session, Map<String, Object> map) throws UnsupportedEncodingException {
    System.out.println("热门标签主页");
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
      System.out.println(
          "获取统计数据: " + "\tnotifiedNUmber : " + notifiedNumber + "\ttopicNumber : " + topicNumber);
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
    String[] strings = stringBuilder.toString().split(";");
    //    使用这个方法得到的只是一个ArrayList代理，因此里面的某些方法如： add(),remove() 无法使用
    //    解决方法就是使用Iterator或者转换为ArrayList
    List<String> tagslist = Arrays.asList(strings);
    tagslist = new ArrayList<>(tagslist);
    for (int i = 0; i < tagslist.size(); i++) {
      for (int j = i + 1; j < tagslist.size(); j++) {
        if (tagslist.get(i).equals(tagslist.get(j))) {
          tagslist.remove(j);
          j--;
        }
      }
    }
    tags = tagslist;
    Pagination<Topic> tagTopics;
    //    取得第一个标签的分享数据
    if (tag.equals("default")) {
      tagTopics = topicService.getTopicsPageByTags(tags.iterator().next(), page, TOPICPAGESIZE);
    } else {
      tag = java.net.URLDecoder.decode(tag, "utf8");
      tagTopics = topicService.getTopicsPageByTags(tag, page, TOPICPAGESIZE);
    }
    //   将所有标签写入session
    session.setAttribute("tags", tags);
    //    将第一个标签对应的分页分享写入session
    session.setAttribute("tagTopics", tagTopics);
    //    取消标识为达人分类
    session.setAttribute("showTopPeople", "false");
    //    取消标识为分享分类
    session.setAttribute("showTopic", "false");
    //    标识为热门标签分类
    session.setAttribute("showPopularTags", "true");
    return HOME;
  }

}
