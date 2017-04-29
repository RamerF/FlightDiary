/*
 *
 */

package org.ramer.diary.exception;

/**
 * 非法访问异常处理
 * @author ramer
 *
 */
public class IllegalAccessException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public IllegalAccessException(String message) {
    super(message);
  }

  public IllegalAccessException() {
  }
}
