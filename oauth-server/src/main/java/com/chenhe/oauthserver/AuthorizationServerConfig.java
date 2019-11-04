package com.chenhe.oauthserver;

import com.chenhe.oauthserver.service.RedisAuthenticationCodeServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


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
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisAuthenticationCodeServices redisAuthenticationCodeServices;

    @Autowired
    private DataSource dataSource;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }


    /**
     * AuthorizationEndpoint可以通过以下方式配置支持的授权类型AuthorizationServerEndpointsConfigurer,
     * 默认情况下，所有授权类型均受支持，除了密码（有关如何切换它的详细信息，请参见下文）。以下属性会影响授权类型：
     * authenticationManager：通过注入密码授权被打开AuthenticationManager。
     * userDetailsService：如果您注入UserDetailsService或者全局配置（例如a GlobalAuthenticationManagerConfigurer），则刷新令牌授权将包含对用户详细信息的检查，以确保该帐户仍然活动
     * authorizationCodeServices：定义AuthorizationCodeServices授权代码授权的授权代码服务（实例）。
     * implicitGrantService：在批准期间管理状态。
     * tokenGranter：（TokenGranter完全控制授予和忽略上述其他属性）
     * 在XML授予类型中包含作为子元素authorization-server。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(this.authenticationManager);
                //.accessTokenConverter(accessTokenConverter());
                //授权码存储
                //.authorizationCodeServices(redisAuthenticationCodeServices);
        //endpoints.approvalStore(approvalStore());
        //endpoints.userDetailsService(myUserDetailService);
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束.
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单认证
        security.allowFormAuthenticationForClients();
    }


    /**
     * 将clientDetailsServiceConfigurer(从回调AuthorizationServerConfigurer)可以用来存在内存或JDBC实现客户端的细节服务来定义,
     * 客户端的重要属性是:
     * clientId：（必填）客户端ID。
     * secret:(可信客户端需要）客户机密码（如果有）。
     * scope：客户受限的范围。如果范围未定义或为空（默认值），客户端不受范围限制。
     * authorizedGrantTypes：授予客户端使用授权的类型。默认值为空。
     * authorities授予客户的授权机构（普通的Spring Security权威机构）
     * <p>
     * 客户端的详细信息可以通过直接访问底层商店（例如，在数据库表中JdbcClientDetailsService）
     * 或通过ClientDetailsManager接口（这两种实现ClientDetailsService也实现）来更新运行的应用程序。
     * <p>
     * 注意：JDBC服务的架构未与库一起打包（因为在实践中可能需要使用太多变体）
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //默认值InMemoryTokenStore对于单个服务器是完全正常的（即，在发生故障的情况下，低流量和热备份备份服务器）。大多数项目可以从这里开始，也可以在开发模式下运行，以便轻松启动没有依赖关系的服务器。
        //这JdbcTokenStore是同一件事的JDBC版本，它将令牌数据存储在关系数据库中。如果您可以在服务器之间共享数据库，则可以使用JDBC版本，如果只有一个，则扩展同一服务器的实例，或者如果有多个组件，则授权和资源服务器。要使用JdbcTokenStore你需要“spring-jdbc”的类路径。
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /**
             * 重写增强token的方法
             * 自定义返回相应的信息
             * @param accessToken
             * @param authentication
             * @return
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String userName = authentication.getUserAuthentication().getName();
                // 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
                User user = (User) authentication.getUserAuthentication().getPrincipal();
                /** 自定义一些token属性 ***/
                final Map<String, Object> additionalInformation = new HashMap<>();
                additionalInformation.put("userName", userName);
                additionalInformation.put("roles", user.getAuthorities());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }
        };
        // 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
        accessTokenConverter.setSigningKey("123");
        return accessTokenConverter;
    }

}
