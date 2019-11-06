package com.chenhe.oauthserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author chenhe
 * @date 2019-11-04 18:10
 * @desc  OAuth 资源服务器配置,需要通过access_token来访问.
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceConfiguration  extends ResourceServerConfigurerAdapter{
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .authorizeRequests().antMatchers("/resource/**").hasRole("USER")
                .and()
                .formLogin().permitAll();

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resourceId");
        super.configure(resources);
    }


}
