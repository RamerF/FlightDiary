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
    return setMessage(modelAndView, exception);
  }

  private ModelAndView setMessage(ModelAndView modelAndView, Exception exception) {
    String ex = exception.getClass().getSimpleName();
    String errorMessage = exception.getMessage();
    System.out.println(ex + " == " + errorMessage);
    switch (ex) {
      case "IllegalAccessException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "非法访问" : errorMessage;
        break;

      case "LinkInvalidException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "链接已失效" : errorMessage;
        break;

      case "NoPictureException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "未选择图片" : errorMessage;
        break;

      case "SystemWrongException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "系统出错了" : errorMessage;
        break;

      case "UserExistException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "用户名已存在" : errorMessage;
        break;

      case "UsernameOrPasswordNotMatchException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "用户名或密码不匹配" : errorMessage;
        break;

      case "UserNotExistException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "用户名不存在存在" : errorMessage;
        break;

      case "UserNotLoginException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "用户未登录" : errorMessage;
        break;
      case "PasswordNotMatchException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "密码不匹配" : errorMessage;
        break;
      case "EmailFormatErrorException":
        errorMessage = (errorMessage == null || errorMessage == "") ? "邮箱格式错误" : errorMessage;
        break;

      default:
        errorMessage = "系统出错了";
        break;
    }
    modelAndView.addObject("errorMessage", errorMessage);
    modelAndView.setViewName("error");
    return modelAndView;
  }

}
