package com.chenhe.oauthserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author chenhe
 * @date 2019-11-04 15:18
 * @desc redis存储授权码(code),部署多台认证中心的时候,code可以共享,多机都可以做认证.
 */
@Slf4j
@Service
public class AuthCodeRedisSaveService extends RandomValueAuthorizationCodeServices {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String AUTH_CODE_KEY = "AUTH_CODE";

    public AuthCodeRedisSaveService() {
    }

    @Override
    protected void store(String code, OAuth2Authentication oAuth2Authentication) {
        log.info("存储令牌:{}",code);
        if (!StringUtils.isEmpty(code)){
            redisTemplate.opsForHash().put(AUTH_CODE_KEY, code, oAuth2Authentication);
        }
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        log.info("删除令牌:{}",code);
        if (code == null || "".equals(code)) {
            return null;
        }

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) redisTemplate.opsForHash().get(AUTH_CODE_KEY, code);
        if (oAuth2Authentication != null) {
            redisTemplate.delete(AUTH_CODE_KEY);
        }
        return oAuth2Authentication;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
        System.out.println(encoder.matches("123456","$2a$10$wmlR/oBD83NILONMRcu.1.pJ35LU0kusXuP7zD6euU4KhWYxjymAi"));
    }
}
