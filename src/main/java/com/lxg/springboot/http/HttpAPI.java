package com.lxg.springboot.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class HttpAPI {

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private RequestConfig requestConfig;

    private static final String REQUEST_FAILED = "请求失败, 系统异常";

    public String Get(String url) {
        return sendGet(new HttpGet(url));
    }

    public String GetMap(String url, Map<String, String> params) {
        HttpGet httpGet = new HttpGet(buildGetParams(url, params));
        return sendGet(httpGet);
    }

    public String GetMap(String url, Header header, Map<String, String> params) {
        HttpGet httpGet = new HttpGet(buildGetParams(url, params));
        httpGet.setHeader(header);
        return sendGet(httpGet);
    }

    private String sendGet(HttpGet httpGet) {
        httpGet.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
            } else {
                return REQUEST_FAILED + " response status: " + statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return REQUEST_FAILED;
        }
    }

    private String buildGetParams(String url, Map<String, String> params) {
        if (params == null || params.size() == 0)
            return url;
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            String value = key == null ? "" : params.get(key);
            if (sb.length() == 0)
                sb.append(key).append("=").append(value);
            else
                sb.append("&").append(key).append("=").append(value);
        }
        return url + "?" + sb;
    }

    public String PostJson(String url, String jsonString) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            StringEntity s = new StringEntity(jsonString);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            httpPost.setEntity(s);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
            } else {
                return REQUEST_FAILED + " response status: " + statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return REQUEST_FAILED;
        }
    }

    public String PostMap(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig.custom().setConnectTimeout(4500).setConnectionRequestTimeout(4500).build());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(buildPostParams(params)));
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
            } else {
                return REQUEST_FAILED + " response status: " + statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return REQUEST_FAILED;
        }
    }

    private List<NameValuePair> buildPostParams(Map<String, String> params) {
        List<NameValuePair> result = new ArrayList<>();
        for (String key : params.keySet())
            result.add(new BasicNameValuePair(key != null ? key : "", params.get(key)));
        return result;
    }

}