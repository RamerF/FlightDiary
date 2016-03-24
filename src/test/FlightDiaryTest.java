/*
 *
 */
package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;

import org.ramer.diary.util.Encrypt;

/**
 * 项目测试类
 * @author ramer
 *
 */
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
}
