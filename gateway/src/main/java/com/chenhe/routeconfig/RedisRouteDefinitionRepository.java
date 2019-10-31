package com.chenhe.routeconfig;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhe
 * @date 2019-10-31 17:21
 * @desc
 */
@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {
    @Autowired
    RedisTemplate redisTemplate;

    private final String GATEWAY_ROUTES = "gateway:routes";

    Logger logger = LoggerFactory.getLogger(RedisRouteDefinitionRepository.class);

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        logger.info("加载缓存路由关系");
        Gson gson = new Gson();
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        redisTemplate.opsForHash().values(GATEWAY_ROUTES).forEach(routeDefinition -> {
            routeDefinitions.add(gson.fromJson(routeDefinition.toString(), RouteDefinition.class));
        });
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        logger.info("保存路由关系");
        Gson gson = new Gson();
        return route.flatMap(routeDefinition -> {
            redisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(), gson.toJson(routeDefinition));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        logger.info("删除路由关系");
        return routeId.flatMap(id -> {
            if (redisTemplate.opsForHash().hasKey(GATEWAY_ROUTES, id)) {
                redisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("路由文件没有找到:" + routeId)));
        });
    }
}
