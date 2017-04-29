/*
 *
 */
package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

/**
 * 时间测试类
 * @author ramer
 *
 */
public class TimeTest {

  /**
   * 测试时间
   */
  @Test
  public void testTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
    String date = simpleDateFormat.format(Calendar.getInstance().getTime());
    System.out.println(date);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 5);
    String date2 = simpleDateFormat.format(calendar.getTime());
    System.out.println(date2);
    System.out.println(date.compareTo(date2));
  }

}
