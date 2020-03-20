package com.chenhe.provider.controller;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author chenhe
 * @date 2019-07-09 11:32
 * @desc
 */
/*@RestController
@RequestMapping("api")*/
public class ApiController {

    @Value("${server.port}")
    String port;

    //@RequestMapping("time")
    public String service() {
        String response = "response port : " +  port + " - " + System.currentTimeMillis();
        return response;
    }
}
