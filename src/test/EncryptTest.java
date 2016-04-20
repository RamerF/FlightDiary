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
    String string = "1874890499@qq.com";
    String encoded = Encrypt.execEncrypt(string, true);
    System.out.println(encoded);
  }

}
