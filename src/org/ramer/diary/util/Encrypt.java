/*
 *
 */
package org.ramer.diary.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密类
 * @author ramer
 */
public class Encrypt {

  /**
   * @param string 要加密的字符串
   * @return 返回已加密的字符串
   */
  public static String execEncrypt(String string) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      System.out.println("加密出现错误");
    }
    char[] charArr = string.toCharArray();
    byte[] byteArr = new byte[charArr.length];
    for (int i = 0; i < charArr.length; i++) {
      byteArr[i] = (byte) charArr[i];
    }
    byteArr = messageDigest.digest(byteArr);
    StringBuilder stringBuilder = new StringBuilder();
    for (byte element : byteArr) {
      int val = element & 0xFF;
      if (val < 16) {
        stringBuilder.append("0");
      }
      stringBuilder.append(Integer.toHexString(val));
    }
    string = doubleEncrypet(stringBuilder.toString());
    return string;

  }

  /**
   * 再次加密: 利用得到的32位MD5字符串,取五个字符加上ramer
   * @param string 需要二次加密的字符串
   * @return 加密字符串
   */
  public static String doubleEncrypet(String string) {
    StringBuilder encryptString = new StringBuilder();
    string = string.substring(10, 15);
    String string2 = "ramer";
    for (int i = 0; i < string.length(); i++) {
      encryptString.append(string.charAt(i) + "" + string2.charAt(i));
    }
    return encryptString.toString();
  }
}
