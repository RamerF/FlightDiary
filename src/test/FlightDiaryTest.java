/*
 *
 */
package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.Pagination;

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
    String string = Encrypt.execEncrypt("Jelly");
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

  @Test
  public void testGetTopicPageByCity() {
    Pagination<Topic> pageTopic = userService.getTopicsPageByCity("重庆", 2, 4);
    List<Topic> topics = pageTopic.getContent();
    for (Topic topic : topics) {
      System.out.println("name : " + topic.getContent());
    }
  }

}
