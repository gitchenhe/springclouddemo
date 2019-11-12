package com.chenhe.oauthserver.access;

import com.google.gson.Gson;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * @author chenhe
 * @date 2019-11-07 16:30
 * @desc token 生成器
 */
public class JwtAccessToken extends JwtAccessTokenConverter {
    public static final String USER_INFO = "USER_INFO";

    /**
     * 解析token
     * @param value
     * @param map
     * @return
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        OAuth2AccessToken oAuth2AccessToken = super.extractAccessToken(value,map);
        convertData(oAuth2AccessToken, oAuth2AccessToken.getAdditionalInformation());
        return super.extractAccessToken(value, map);
    }

    private void convertData(OAuth2AccessToken accessToken,  Map<String, ?> map) {
        accessToken.getAdditionalInformation().put(USER_INFO,convertUserData(map.get(USER_INFO)));
    }

    private Object convertUserData(Object o) {
        Gson gson = new Gson();
        String json =gson.toJson(o);
        UserInfo user = gson.fromJson(json,UserInfo.class);
        return user;
    }

    /**
     * 生成token 携带用户信息
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        UserInfo userInfo = ((UserDetail) authentication.getPrincipal()).getUserInfo();
        userInfo.setPassword(null);
        //将用户信息添加到token额外信息中
        defaultOAuth2AccessToken.getAdditionalInformation().put(USER_INFO,userInfo);
        return super.enhance(defaultOAuth2AccessToken, authentication);
    }
}
