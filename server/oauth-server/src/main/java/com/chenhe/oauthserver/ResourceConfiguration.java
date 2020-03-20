package com.chenhe.oauthserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author chenhe
 * @date 2019-11-04 18:10
 * @desc  OAuth 资源服务器配置,需要通过access_token来访问.
 */
@Slf4j
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceConfiguration  extends ResourceServerConfigurerAdapter{

    private final String RESOURCE_ID = "resourceId";

    @Autowired
    ResourceServerTokenServices resourceServerTokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/resources/**").permitAll();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources
                //.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
                .tokenServices(resourceServerTokenServices)
                .resourceId(RESOURCE_ID);


       /* DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new PigxUserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);

        PigxCustomTokenServices tokenServices = new PigxCustomTokenServices();

        // 这里的签名key 保持和认证中心一致
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        converter.setVerifier(new MacSigner("123"));
        JwtTokenStore jwtTokenStore = new JwtTokenStore(converter);
        tokenServices.setTokenStore(jwtTokenStore);
        tokenServices.setJwtAccessTokenConverter(converter);
        tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);

        resources
                .authenticationEntryPoint(resourceAuthExceptionEntryPoint)
                .tokenServices(tokenServices);*/
    }


}
