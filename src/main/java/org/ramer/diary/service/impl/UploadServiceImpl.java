package org.ramer.diary.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.ramer.diary.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sequarius on 2016/11/23.
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService{
    public static final String IOVIP_QBOX_ME_HOST = "iovip-z2.qbox.me";
    public static final String RS_HOST = "rs.qiniu.com";
    @Value("${diary.qiniu.access}")
    private String ACCESS_KEY;
    @Value("${diary.qiniu.secret}")
    private String SECRET_KEY;
    @Value("${diary.qiniu.bucketName}")
    private String BUCKET_NAME;
    private Auth auth;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public String getUploadToken() {
        return auth.uploadToken(BUCKET_NAME);
    }

    @Override
    public JSONObject fetchResourceByUrl(String url) {
        String entityName = getUrlEntityName(url);
        StringBuilder sb = new StringBuilder("https://").append(IOVIP_QBOX_ME_HOST);
        log.debug("url=={}", url);
        StringBuilder urlSb = new StringBuilder().append("/fetch/").append(UrlSafeBase64.encodeToString(url))
                .append("/to/");
        if (entityName != null) {
            urlSb.append(UrlSafeBase64.encodeToString(String.join(":", BUCKET_NAME, entityName)));
        } else {
            urlSb.append(UrlSafeBase64.encodeToString(BUCKET_NAME));
        }
        log.debug("url in=={}", urlSb.toString());
        RequestBody body = RequestBody.create(null, new byte[] {});
        String uploadToken = (String) auth.authorization(urlSb.toString()).get("Authorization");
        log.debug("upload toke=={}", uploadToken);
        Request request = new Request.Builder().addHeader("Host", IOVIP_QBOX_ME_HOST)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Authorization", uploadToken)
                .url(sb.append(urlSb).toString()).post(body).build();
        log.debug("request=={}", request);
        return processResponse(request);
    }

    @Override
    public JSONObject deleteResourceByUrl(String url) {
        String entityName = getUrlEntityName(url);
        StringBuilder sb = new StringBuilder("http://").append(RS_HOST);
        log.debug("entityName=={}", entityName);
        StringBuilder urlSb = new StringBuilder().append("/delete/")
                .append(UrlSafeBase64.encodeToString(String.join(":", BUCKET_NAME, entityName)));

        log.debug("url in=={}", urlSb.toString());
        RequestBody body = RequestBody.create(null, new byte[] {});
        String token = (String) auth.authorization(urlSb.toString()).get("Authorization");
        log.debug("upload toke=={}", token);
        Request request = new Request.Builder().addHeader("Host", RS_HOST)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Authorization", token)
                .url(sb.append(urlSb).toString()).post(body).build();
        log.debug("request=={}", request);
        return processResponse(request);
    }

    private JSONObject processResponse(Request request) {
        try {
            Response response = okHttpClient.newCall(request).execute();
            log.debug("response code=={}", response.code());
            JSONObject obj = JSON.parseObject(response.body().string());
            if (obj == null) {
                obj = new JSONObject();
            }
            obj.put("code", response.code());
            return obj;
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            JSONObject errorObj = new JSONObject();
            errorObj.put("code", -100);
            return errorObj;
        }
    }

    @PostConstruct
    public void init() {
        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    }

    public String getUrlEntityName(String url) {
        if (url == null) {
            return null;
        }
        if (url.endsWith("/0")) {
            url = url.substring(0, url.length() - 2);
        }
        String regExp = "^https*://.*/(.*)$";
        Pattern r = Pattern.compile(regExp);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
