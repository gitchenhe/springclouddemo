package com.chenhe.routeconfig;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author chenhe
 * @date 2019-10-31 17:59
 * @desc 限流默认策略
 */
@Configuration
public class RequestRateLimiterConfig {
    @Bean
    public KeyResolver remoteAddrKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
