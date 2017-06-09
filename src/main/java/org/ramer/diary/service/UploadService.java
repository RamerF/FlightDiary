package org.ramer.diary.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Sequarius on 2016/11/23.
 */
public interface UploadService {
    String getUploadToken();

    JSONObject fetchResourceByUrl(String url);

    JSONObject deleteResourceByUrl(String url);

    String getUrlEntityName(String url);
}
