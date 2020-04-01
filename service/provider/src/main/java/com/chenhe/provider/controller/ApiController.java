package com.chenhe.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @date 2019-07-09 11:32
 * @desc
 */
@RestController
@RequestMapping("api")
@RefreshScope
public class ApiController {

    @Value("${server.port}")
    String port;

    @Value("${test.value:test}")
    String testValue;

    @RequestMapping("time")
    public String service() {
        String response = "response port : " +  port + " - " + System.currentTimeMillis() + " - " + testValue;
        return response;
    }
}
