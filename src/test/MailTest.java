/*
 *
 */
package test;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.ramer.diary.util.MailUtils;

/**
 * 邮箱工具测试类
 * @author ramer
 *
 */
public class MailTest {

  /**
   * 测试发送邮件
   */
  @Test
  public void testSendMail() {
    String mailTo = "1874890499@qq.com";
    MailUtils.sendMail(mailTo, "FlightDiary", "内容");
  }

  /**
   * 邮件正则表达式测试
   */
  @Test
  public void testIsEmail() {
    Pattern pattern = Pattern.compile(
        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    Matcher matcher = pattern.matcher("feng1390635973@aa.cn");
    System.out.println(matcher.matches());
    @SuppressWarnings("resource")
    Scanner scanner = new Scanner(System.in);
    String email = "";
    while (!(email = scanner.next()).equals("exit")) {
      MailUtils.isEmail(email);
    }
  }
}
