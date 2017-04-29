package org.ramer.diary.handler;

import org.ramer.diary.constant.PageConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定向到错误页面
 */
@Controller
public class ForwardError {
  //全局出错页面
  private final String ERROR = PageConstant.ERROR.toString();

  /**
   *  重定向到错误页面.
   *
   * @return 重定向到错误页面
   */
  @RequestMapping("/error")
  public String forwardError() {
    return ERROR.substring(10);
  }

}
