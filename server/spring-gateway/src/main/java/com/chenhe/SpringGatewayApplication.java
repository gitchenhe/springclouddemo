package com.chenhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SpringGatewayApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(SpringGatewayApplication.class, args);
    }

    String[] ips = {"localhost:1004"};
    @Bean
    public RouteLocator customerLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("path_route", r->r.path("/get").uri("https://www.baidu.com"))
                .route("host_route", r->r.host(ips).uri("https://www.taobao.com"))
                //.route("rewrite_route", r-> r.host(""))
                .build();
    }
}
