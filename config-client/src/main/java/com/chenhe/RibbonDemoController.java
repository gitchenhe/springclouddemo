package com.chenhe;

import com.chenhe.service.FeignService;
import com.chenhe.service.RibbonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @Date 2018-07-09 18:17
 * @desc Ribbon 可单独部署
 **/
@RestController
@RefreshScope
public class RibbonDemoController {
    @Autowired
    RibbonService ribbonDemoService;
    @Autowired
    FeignService feignService;

    Logger logger = LoggerFactory.getLogger(RibbonDemoController.class);

    @RequestMapping("testFeign")
    public String test(){
        logger.info("[测试 feign 服务] ---");
        return feignService.say("nihao");
    }

    @RequestMapping("testRibbon")
    public String test2(){
        logger.info("[测试 Ribbon 服务] ---");
        return ribbonDemoService.hiService("nihao");
    }

    @Value("${hello}")
    String hello;

    @RequestMapping("hello")
    public String hello(){
        return hello;
    }
}
