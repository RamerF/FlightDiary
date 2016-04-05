/*
 *
 */

package org.ramer.diary.exception;

/**
 * 用户已存在异常处理
 * @author ramer
 *
 */
public class UserNotExistException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public UserNotExistException(String message) {
    super(message);
  }

  public UserNotExistException() {
  }
}
