package com.chenhe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author chenhe
 * @date 2020-03-24 14:12
 * @desc 资源服务配置
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
                .requestMatchers()
                .antMatchers("/api/**");*/

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/userInfo/**").hasAuthority("read_userinfo")
                .antMatchers("/resources/**").permitAll();
    }

}
