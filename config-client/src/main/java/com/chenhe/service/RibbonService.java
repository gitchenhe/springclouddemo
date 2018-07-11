package com.chenhe.service;

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

    public String hiService(String name) {
        return restTemplate.getForObject("http://hello-service/h?msg="+name,String.class);
    }
}
