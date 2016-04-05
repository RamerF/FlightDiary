/*
 *
 */

package org.ramer.diary.exception;

/**
 * 密码不匹配异常处理
 * @author ramer
 *
 */
public class PasswordNotMatchException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PasswordNotMatchException(String message) {
    super(message);
  }

  public PasswordNotMatchException() {
  }
}
