/*
 *
 */

package org.ramer.diary.exception;

/**
 * 用户已存在异常处理
 * @author ramer
 *
 */
public class EmailFormatErrorException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public EmailFormatErrorException(String message) {
    super(message);
  }

  public EmailFormatErrorException() {
  }
}
