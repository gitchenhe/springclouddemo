package com.chenhe.oauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author chenhe
 * @date 2019-11-07 18:02
 * @desc 从token store里读取token,转换成
 */
@Service
public class OauthServerTokenServices implements ResourceServerTokenServices {

    @Autowired
    private TokenStore tokenStore;

    private DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        UserAuthenticationConverter userTokenConverter = new OauthUserAuthenticationConverter();
        defaultAccessTokenConverter.setUserTokenConverter(userTokenConverter);
        //从store里读取token
        OAuth2AccessToken oAuth2AccessToken =  readAccessToken(accessToken);
        //转换token获取有用字段
        Map<String, ?> map = jwtAccessTokenConverter.convertAccessToken(oAuth2AccessToken, oAuth2Authentication);
        //提取身份信息
        return defaultAccessTokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
        if (null == token){
            throw new InvalidTokenException("Check whether the token is valid!");
        }
        return token;
    }
}
