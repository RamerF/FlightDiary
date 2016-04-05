/*
 *
 */

package org.ramer.diary.exception;

/**
 * 非法访问异常处理
 * @author ramer
 *
 */
public class NoPictureException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NoPictureException(String message) {
    super(message);
  }

  public NoPictureException() {
  }
}
