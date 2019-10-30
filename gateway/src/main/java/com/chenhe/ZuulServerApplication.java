package com.chenhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 路由网关
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class ZuulServerApplication {
    public static void main(String[] args){
        SpringApplication.run(ZuulServerApplication.class,args);
    }
}
