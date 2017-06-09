/*
 *
 */

package org.ramer.diary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 用户未登录异常处理
 * @author ramer
 *
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "用户未登录")
public class UserNotLoginException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UserNotLoginException(String message) {
        super(message);
    }

    public UserNotLoginException() {
    }
}
