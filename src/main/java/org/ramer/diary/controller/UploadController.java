package org.ramer.diary.controller;

import com.alibaba.fastjson.JSONObject;
import org.ramer.diary.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by RAMER on 6/9/2017.
 */
@Controller
public class UploadController{
    @Resource
    UploadService uploadService;

    @RequestMapping("/upload/token")
    @ResponseBody
    public JSONObject upLoadToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uptoken", uploadService.getUploadToken());
        return jsonObject;
    }

    @GetMapping("/upload")
    public String forwardQiniuUpload() {
        return "qiniu_upload";
    }
}
