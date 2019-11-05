package com.chenhe.oauthserver;

import com.chenhe.oauthserver.service.RedisAuthenticationCodeServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * @author chenhe
 * @date 2019-11-04 14:21
 * @desc  OAuth 授权服务器配置
 */
@Slf4j
@Configuration
@EnableAuthorizationServer //启动OAuth2.0授权服务机制
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${resource.id:spring-boot-application}")
    private String resourceId;

    @Value("${access_token.validity_period:36000}")
    private int accessTokenValiditySeconds = 36000;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisAuthenticationCodeServices redisAuthenticationCodeServices;

    @Autowired
    private DataSource dataSource;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(redisTokenStore())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
        endpoints.tokenServices(defaultTokenServices());
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     *
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12); // token有效期自定义设置，默认12小时
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);//默认30天，这里修改
        return tokenServices;
    }

    private ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束.
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
        security.checkTokenAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients();
    }


    /**
     * 用来配置客户端详情
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //默认值InMemoryTokenStore对于单个服务器是完全正常的（即，在发生故障的情况下，低流量和热备份备份服务器）。大多数项目可以从这里开始，也可以在开发模式下运行，以便轻松启动没有依赖关系的服务器。
        //这JdbcTokenStore是同一件事的JDBC版本，它将令牌数据存储在关系数据库中。如果您可以在服务器之间共享数据库，则可以使用JDBC版本，如果只有一个，则扩展同一服务器的实例，或者如果有多个组件，则授权和资源服务器。要使用JdbcTokenStore你需要“spring-jdbc”的类路径。
        clients.withClientDetails(clientDetails());
    }

}
