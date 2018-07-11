package com.chenhe.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenhe
 * @Date 2018-07-10 17:37
 * @desc  Feign 服务 可单独部署
 **/
@FeignClient(value = "hello-service")
public interface FeignService {

    @RequestMapping(value = "h", method = RequestMethod.GET)
    String say(@RequestParam(value = "msg") String msg);
}
