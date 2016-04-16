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
   * 32位MD5加密
   * @param isEmail 是否是邮箱
   * @param string 要加密的字符串
   * @return 返回已加密的字符串
   */
  public static String execEncrypt(String string, boolean isEmail) {
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
    //如果是邮箱特殊处理
    if (isEmail) {
      //首字符
      char c = string.charAt(0);
      String startString = String.valueOf(c);
      //定位@，取index-2 到结束的字符串
      int n = string.indexOf("@");
      String endString = string.substring(n - 2, string.length());
      string = doubleEncrypet(stringBuilder.toString(), startString, endString);
      return string;
    }
    string = doubleEncrypet(stringBuilder.toString());
    return string;

  }

  /**
   * 再次加密: 利用得到的32位MD5字符串,取五个字符加上ramer
   * @param string 需要二次加密的字符串
   * @return 加密字符串
   */
  public static String doubleEncrypet(String string, String... emailString) {
    StringBuilder encryptString = new StringBuilder();
    //取五位字符
    string = string.substring(10, 15);
    //如果是邮箱则该字符串有值
    if (emailString.length > 0) {
      encryptString.append(emailString[0]).append(string).append(emailString[1]);
      System.out.println("邮箱加密完成： " + encryptString);
      return encryptString.toString();
    }
    String string2 = "ramer";
    for (int i = 0; i < string.length(); i++) {
      encryptString.append(string.charAt(i) + "" + string2.charAt(i));
    }
    return encryptString.toString();
  }
}
