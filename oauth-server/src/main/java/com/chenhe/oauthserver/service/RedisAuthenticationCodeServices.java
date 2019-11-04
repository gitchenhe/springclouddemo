package com.chenhe.oauthserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

/**
 * @author chenhe
 * @date 2019-11-04 15:18
 * @desc 自定义为使用redis存储授权码
 */
@Slf4j
@Service
public class RedisAuthenticationCodeServices extends RandomValueAuthorizationCodeServices {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String AUTH_CODE_KEY = "AUTH_CODE";

    public RedisAuthenticationCodeServices() {
    }


    @Override
    protected void store(String code, OAuth2Authentication oAuth2Authentication) {
        log.info("存储令牌:{}",code);
        redisTemplate.opsForHash().put(AUTH_CODE_KEY, code, oAuth2Authentication);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        log.info("删除令牌:{}",code);
        OAuth2Authentication authentication = (OAuth2Authentication) redisTemplate.opsForHash().get(AUTH_CODE_KEY, code);
        if (authentication != null) {
            redisTemplate.opsForHash().delete(AUTH_CODE_KEY, code);
        }
        return authentication;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
        System.out.println(encoder.matches("123456","$2a$10$wmlR/oBD83NILONMRcu.1.pJ35LU0kusXuP7zD6euU4KhWYxjymAi"));
    }
}
