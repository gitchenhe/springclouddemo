package com.chenhe.oauthserver;

import com.chenhe.oauthserver.access.JwtAccessToken;
import com.chenhe.oauthserver.service.AuthCodeRedisSaveService;
import com.chenhe.oauthserver.service.OauthRedisTokenStoreSerializationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;


/**
 * @author chenhe
 * @date 2019-11-04 14:21
 * @desc  OAuth 授权服务器配置
 */
@Slf4j
@Configuration
@EnableAuthorizationServer //启动OAuth2.0授权服务机制
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    AuthCodeRedisSaveService authCodeRedisSaveService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

   @Primary
    @Bean
    TokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore= new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setSerializationStrategy(new OauthRedisTokenStoreSerializationStrategy());
        return redisTokenStore;
    }

    //token存储数据库
//    @Bean
//    public JdbcTokenStore jdbcTokenStore(){
//        return new JdbcTokenStore(dataSource);
//    }


   /* @Bean
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(new JwtAccessToken());
    }*/

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("clientapp")
                .secret("112233")
                .redirectUris("http://localhost:9001/callback")
                // 授权码模式
                .authorizedGrantTypes("authorization_code")
                .scopes("read_userinfo", "read_contacts");
        //clients.withClientDetails(clientDetails());
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

       /* endpoints
                //认证管理器
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                //token存储位置
                .tokenStore(redisTokenStore())
                //token生成方式,配置JwtAccessToken转换器
                .accessTokenConverter(jwtAccessTokenConverter())
                //authCode存储
                .authorizationCodeServices(authCodeRedisSaveService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
*/
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {

        final JwtAccessTokenConverter converter = new JwtAccessToken();
        // 导入证书
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "123456".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauthkey"));
       // converter.setVerifier(new RsaVerifier());
        return converter;
    }


    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     * @return
     */
    /*@Primary
    @Bean*/
   /* public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12); // token有效期自定义设置，默认12小时
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);//默认30天，这里修改
        return tokenServices;
    }
*/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");*/
    }

}
