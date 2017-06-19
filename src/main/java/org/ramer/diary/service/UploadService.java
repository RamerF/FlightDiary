package org.ramer.diary.service;

import com.alibaba.fastjson.JSONObject;

public interface UploadService{
    String getUploadToken();

    JSONObject fetchResourceByUrl(String url);

    JSONObject deleteResourceByUrl(String url);

    String getUrlEntityName(String url);
}
