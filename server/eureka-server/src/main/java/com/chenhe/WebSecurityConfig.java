package com.chenhe;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author chenhe
 * @date 2019-11-04 13:49
 * @desc
 */
@Slf4j
@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${security.white.iplist}")
    public String blackIpList;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (StringUtils.isNotBlank(blackIpList)) {
            String[] ips = blackIpList.split(",");
            for (String ip : ips) {
                log.info("白名单IP:{}", ip);
                http.authorizeRequests()
                        .antMatchers("/actuator/*").access("hasIpAddress('"+ip+"')");
            }
        }

        http.csrf().disable();
        super.configure(http);
    }
}
