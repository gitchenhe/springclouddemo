package com.chenhe.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenhe
 * @Date 2018-07-10 17:56
 * @desc
 **/
@Service
public class RibbonService {

    @Autowired
    RestTemplate restTemplate;

    //当访问不到provider的时候,触发指定方法
    @HystrixCommand(fallbackMethod ="hiError" )
    public String hiService(String name) {
        return restTemplate.getForObject("http://hello-service/h?msg="+name,String.class);
    }


    public String hiError(String name){
        return "服务器挂了!!!";
    }
}
