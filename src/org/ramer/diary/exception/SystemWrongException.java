/*
 *
 */

package org.ramer.diary.exception;

/**
 * 系统出错异常处理
 * @author ramer
 *
 */
public class SystemWrongException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public SystemWrongException(String message) {
    super(message);
  }

  public SystemWrongException() {
  }
}
