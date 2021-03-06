package com.lxg.springboot.controller;

import com.lxg.springboot.http.HttpAPI;
import com.lxg.springboot.http.HttpResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lxg
 * on 2017/2/6.
 */
@RestController
public class HttpClientController {

    @Resource
    private HttpAPI httpAPI;

    @RequestMapping("httpclient")
    public String test() throws Exception {
        HttpResult result = httpAPI.Get("http://www.baidu.com");
        System.out.println(result.getCode());
        System.out.println(result.getBody());
        return result.getBody();
    }

}
