/*
 *
 */
package test;

import org.junit.Test;
import org.ramer.diary.util.Encrypt;

/**
 * 加密类的测试类
 * @author ramer
 *
 */
public class EncryptTest {

  /**
   * 测试加密方法.
   */
  @Test
  public void testEncrypt() {
    String string = "1390635973@qq.com";
    System.out.println("加密字符串： " + string);
    String encoded = Encrypt.execEncrypt(string, true);
    System.out.println(encoded);
  }

}
