package com.chenhe.routeconfig;

import com.chenhe.bean.GateRoute;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

/**
 * @author chenhe
 * @date 2019-10-31 17:43
 * @desc
 */
@Slf4j
@Service
public class GatewayService implements ApplicationEventPublisherAware, CommandLineRunner {

    @Autowired
    RedisRouteDefinitionRepository redisRouteDefinitionRepository;
    ApplicationEventPublisher publisher;
    @Autowired
    JdbcTemplate jdbcTemplate;
    public String save(){
        //从数据库拿到路由配置
        List<GateRoute> gateRoutes = getGateRoute();
        Gson gson = new Gson();
        log.info("网关配置信息 ===> {}",gson.toJson(gateRoutes));

        gateRoutes.forEach(gateWay -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            Map<String,String> predicateParams = new HashMap<>(8);
            //路由断言定义表
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            //过滤器定义表
            FilterDefinition filterDefinition = new FilterDefinition();

            Map<String,String> filterParams = new HashMap<>(8);
            routeDefinition.setId(gateWay.getId().toString());
            predicateDefinition.setName("Path");
            predicateParams.put("pattern","/api"+gateWay.getPath());
            predicateParams.put("pathPattern","/api"+gateWay.getPath());

            URI uri = UriComponentsBuilder.fromUriString(gateWay.getUrl()).build().toUri();
            filterDefinition.setName("RequestRateLimiter");

            //路径去前缀
            filterParams.put("_genkey_0", Optional.ofNullable(gateWay.getStripPrefix()).orElse(new Integer(0)).toString());
            //令牌桶流速: 允许用户每秒处理多少个请求
            filterParams.put("redis-rate-limiter.replenishRate",gateWay.getLimiterRate());
            //令牌通容量: 令牌桶的容量，允许在一秒钟内完成的最大请求数
            filterParams.put("redis-rate-limiter.burstCapacity",gateWay.getLimiterCapacity());
            //限流策略
            filterParams.put("key-resolver","#{@remoteAddrKeyResolver}");

            predicateDefinition.setArgs(predicateParams);
            filterDefinition.setArgs(filterParams);

            routeDefinition.setPredicates(Arrays.asList(predicateDefinition));
            routeDefinition.setFilters(Arrays.asList(filterDefinition));
            routeDefinition.setUri(uri);
            redisRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
        });

        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        return "success";
    }

    public List<GateRoute> getGateRoute() {
        String sql = "select * from t_gateway_router where enabled = 'enable'";
        List<GateRoute> gateRoutes = jdbcTemplate.query(sql, new Object[]{},new BeanPropertyRowMapper<GateRoute>(GateRoute.class));
        return gateRoutes;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void deleteRoute(String routeId){
        redisRouteDefinitionRepository.delete(Mono.just(routeId)).subscribe();
    }

    @Override
    public void run(String... args) throws Exception {
        this.save();
    }
}
