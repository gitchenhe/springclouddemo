package com.chenhe.routeconfig;

import com.chenhe.bean.GateRoute;
import com.google.gson.Gson;
import javafx.scene.input.RotateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
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
    public String save(){
        //从数据库拿到路由配置
        List<GateRoute> gateRoutes = getGateRoute();
        Gson gson = new Gson();
        log.info("网关配置信息 ===> {}",gson.toJson(gateRoutes));

        gateRoutes.forEach(gateWay -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            Map<String,String> predicateParams = new HashMap<>(8);
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            FilterDefinition filterDefinition = new FilterDefinition();

            Map<String,String> filterParams = new HashMap<>(8);
            routeDefinition.setId(gateWay.getId().toString());
            predicateDefinition.setName("Path");
            predicateParams.put("pattern","/api"+gateWay.getPath());
            predicateParams.put("pathPattern","/api"+gateWay.getPath());

            URI uri = UriComponentsBuilder.fromUriString("lb://"+gateWay.getServiceId()).build().toUri();
            filterDefinition.setName("StripPrefix");

            //路径去前缀
            filterParams.put("_genkey_0",gateWay.getStripPrefix().toString());
            //令牌桶流速
            filterParams.put("redis-rate-limiter.replenishRate",gateWay.getLimiterRate());
            //令牌通容量
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

    private List<GateRoute> getGateRoute() {
        List<GateRoute> gateRoutes = new ArrayList<>();

        for (int i=0;i<10;i++){
            GateRoute gateRoute = new GateRoute();
            gateRoutes.add(gateRoute);
        }
        return gateRoutes;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = publisher;
    }

    public void deleteRoute(String routeId){
        redisRouteDefinitionRepository.delete(Mono.just(routeId)).subscribe();
    }

    @Override
    public void run(String... args) throws Exception {
        this.save();
    }
}
