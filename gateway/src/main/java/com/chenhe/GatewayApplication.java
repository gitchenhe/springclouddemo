package com.chenhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * 路由网关
 */
@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/baidu").uri("http://www.baidu.com"))
                .build();
    }
    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class,args);
    }
}
