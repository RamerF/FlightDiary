/*
 *
 */
package org.ramer.diary.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常拦截器
 * 
 * @author ramer
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler implements HandlerExceptionResolver{

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
            Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        log.debug("", exception);
        return setMessage(modelAndView, exception);
    }

    private static ModelAndView setMessage(ModelAndView modelAndView, Exception exception) {
        String ex = exception.getClass().getSimpleName();
        String errorMessage = exception.getMessage();
        switch (ex) {
        case "DiaryException":
            errorMessage = (errorMessage == null || errorMessage == "") ? "系统异常,请稍后再试" : errorMessage;
            break;
        default:
            errorMessage = "系统被程序猿玩儿坏啦，看看其他东东坏没  ^_^\"";
            break;
        }
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
