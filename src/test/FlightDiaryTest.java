/*
 *
 */
package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目测试类
 * @author ramer
 *
 */
@ContextConfiguration("/applicationContext.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class FlightDiaryTest {

  @Autowired
  private TopicService topicService;

  /**
   * Test1.
   */
  @Test
  public void testSimpleDateFormat() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    String date = simpleDateFormat.format(new Date());
    System.out.println(date);
  }

  /**
   * 测试加密
   */
  @Test
  public void testEncrypt() {
    String string = Encrypt.execEncrypt("Jelly", false);
    System.out.println("string = " + string);
  }

  /**
   * 测试邮箱正则表达式
   */
  @SuppressWarnings("resource")
  @Test
  public void testRegex() {
    String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    Scanner scanner = new Scanner(System.in);
    String match = null;
    match = scanner.next();
    while (!match.matches(regex)) {
      System.out.println("string = " + match);
      match = scanner.next();
    }
    System.out.println("true");
  }

  /**
   * 测试过期时间
   */
  @Test
  public void testExpireTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 5);
    System.out.println(calendar.get(Calendar.MINUTE));
  }

  /**
   * 测试页面常量
   */
  @Test
  public void testPageConstant() {
    System.out.println(PageConstant.HOME.toString());
  }

  @Autowired
  private UserService userService;

  /**
   * 测试获取达人:  发表最多分享的用户
   */
  @Test
  public void testGetTopPeople() {
    Pagination<User> pageUser = userService.getTopPeople(1, 3);
    List<User> users = pageUser.getContent();
    System.out.println("当前第 " + pageUser.getNumber() + "页");
    for (User user : users) {
      System.out.println("name : " + user.getName());
    }
  }

  /**
   * 测试通过标签和页码获取分享
   */
  @Test
  public void testGetTopicPageByTags() {
    Pagination<Topic> pageTopic = topicService.getTopicsPageByTags("重庆", 2, 4);
    List<Topic> topics = pageTopic.getContent();
    for (Topic topic : topics) {
      System.out.println("name : " + topic.getContent());
    }
  }

  @Test
  public void testGetAllCities() {
    List<String> tags = topicService.getAllTags();
    System.out.println("标签名 :  ");
    for (String tag : tags) {
      System.out.println("\t" + tag);
    }
    System.out.println("----------------------\n" + "第一个标签 :  " + tags.iterator().next());
  }

  @Test
  public void testSplitTags() throws Exception {
    String tag1 = "纪实";
    String tag2 = "纪实,旅游";
    List<String> tags = new ArrayList<>();
    tags.add(tag1);
    tags.add(tag2);
    StringBuilder stringBuilder = new StringBuilder();
    for (String string : tags) {
      stringBuilder.append(string + ",");
    }
    String[] strings = stringBuilder.toString().split(",");
    List<String> tagslist = Arrays.asList(strings);
    tagslist = new ArrayList<>(tagslist);
    System.out.println("list---------------------" + tagslist);
    for (int i = 0; i < tagslist.size(); i++) {
      for (int j = i + 1; j < tagslist.size(); j++) {
        if (tagslist.get(i).equals(tagslist.get(j))) {
          tagslist.remove(j);
          j--;
        }
      }
    }
    for (String string : tagslist) {
      System.out.println("-------------------" + string);

    }
  }
}
