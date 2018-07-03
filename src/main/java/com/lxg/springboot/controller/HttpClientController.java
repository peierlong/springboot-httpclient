package com.lxg.springboot.controller;

import com.lxg.springboot.http.HttpAPI;
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
        String str = httpAPI.Get("http://www.baidu.com");
        System.out.println(str);
        return "hello";
    }
}
