/*
 *
 */
package org.ramer.diary.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ramer
 *
 */
@ControllerAdvice
public class MyExceptionHandler implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
      Object object, Exception exception) {
    System.out.println("异常处理");
    ModelAndView modelAndView = new ModelAndView();
    String errorMessage = (exception.getMessage() == null) ? "用户未登录" : (exception.getMessage());
    if (exception instanceof UserNotLoginException) {
      modelAndView.addObject("errorMessage", errorMessage);
    }
    modelAndView.setViewName("error");
    return modelAndView;
  }

}
