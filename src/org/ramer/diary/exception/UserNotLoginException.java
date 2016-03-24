/*
 *
 */

package org.ramer.diary.exception;

/**
 * 用户未登录异常处理
 * @author ramer
 *
 */
//@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "用户未登录")
public class UserNotLoginException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new user not login exception.
   *
   * @param message the message
   */
  public UserNotLoginException(String message) {
    super(message);
  }

  /**
   * Instantiates a new user not login exception.
   */
  public UserNotLoginException() {
  }
}
