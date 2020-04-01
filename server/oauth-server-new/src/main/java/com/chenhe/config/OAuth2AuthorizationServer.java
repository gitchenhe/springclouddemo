package com.chenhe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * @author chenhe
 * @date 2020-03-24 14:11
 * @desc 授权服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 授权码模式
     * 1.拿到code  (get) http://localhost:8081/oauth/authorize?client_id=clientapp&redirect_uri=http://localhost:9001/callback&response_type=code&scope=read_userinfo
     * 2.code换取token (post) http://localhost:8081/oauth/token?code=ofWLPj&grant_type=authorization_code&redirect_uri=http://localhost:9001/callback&scope=read_userinfo
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.inMemory()
                .withClient("clientapp")
                .secret("{noop}112233")
                .redirectUris("http://localhost:8081/callback")
                // 授权码模式
                .authorizedGrantTypes("authorization_code")
                //隐式授权
                //.authorizedGrantTypes("implicit")
                .scopes("read_userinfo", "read_contacts");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 使用默认的验证管理器和用户信息服务
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
