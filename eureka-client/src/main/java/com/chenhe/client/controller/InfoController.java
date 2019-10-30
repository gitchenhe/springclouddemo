package com.chenhe.client.controller;

import com.chenhe.client.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenhe
 * @date 2019-07-09 11:39
 * @desc
 */
@RestController
public class InfoController {
    @Autowired
    RemoteService remoteService;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("info")
    public Object info(){
        return remoteService.timestamp();
    }

    @RequestMapping("time")
    public String hello(){
        return restTemplate.getForObject("http://PROVIDER/api/time",String.class);
    }
}
