/*
 *
 */

package org.ramer.diary.exception;

/**
 * 用户已存在异常处理
 * @author ramer
 *
 */
public class EmailExistException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public EmailExistException(String message) {
    super(message);
  }

  public EmailExistException() {
  }
}
