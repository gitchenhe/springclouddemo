package com.chenhe.client.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author chenhe
 * @date 2019-10-28 11:27
 * @desc
 */
@Component
public class ClientHealthIndicator implements HealthIndicator {
    int i=0;
    @Override
    public Health health() {

        i++;
        if (i%2==0){
            return Health.down().withDetail("描述信息:","服务挂了").build();
        }else{
            return Health.up().withDetail("描述信息","服务一切OK").build();
        }
    }
}
