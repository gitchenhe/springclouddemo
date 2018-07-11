package com.chenhe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenhe
 * @Date 2018-07-06 16:24
 * @desc
 **/
@RestController
public class HelloController{
        private final Logger logger = LoggerFactory.getLogger(getClass());
        @Autowired
        private DiscoveryClient client;

        @Value("${server.port}")
        private String port;

        @RequestMapping(value = "/hello", method = RequestMethod.GET)
        public String index() {
                List<ServiceInstance> instances = client.getInstances("hello-service");
                for (int i = 0; i < instances.size(); i++) {
                        logger.info("/hello,host:" + instances.get(i).getHost() + ",service_id:" + instances.get(i).getServiceId());
                }
                return "Hello World";
        }

        @RequestMapping(value = "/h", method = RequestMethod.GET)
        public String hello(@RequestParam String msg) {
                logger.info("端口 - {} 收到消息 : {}",port,msg);
                return "Hello World - " + port + " - " +  msg;
        }


}
