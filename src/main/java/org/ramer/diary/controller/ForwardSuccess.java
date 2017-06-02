package org.ramer.diary.controller;

import org.ramer.diary.constant.PageConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定向到成功页面
 */
@Controller
public class ForwardSuccess{
    //全局成功页面
    private final String SUCCESS = PageConstant.SUCCESS;

    /**
     * 重定向到成功页面.
     *
     * @return 重定向到成功页面
     */
    @GetMapping("/success")
    public String forwardSuccess() {
        return SUCCESS.substring(10);
    }

}
