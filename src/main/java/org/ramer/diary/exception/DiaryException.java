/*
 *
 */

package org.ramer.diary.exception;

/**
 * 默认异常
 * @author ramer
 *
 */
public class DiaryException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DiaryException(String message) {
        super(message);
    }

    public DiaryException() {
    }
}
