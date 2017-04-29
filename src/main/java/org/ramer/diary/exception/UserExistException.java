/*
 *
 */

package org.ramer.diary.exception;

/**
 * 用户已存在异常处理
 * @author ramer
 *
 */
public class UserExistException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public UserExistException(String message) {
    super(message);
  }

  public UserExistException() {
  }
}
