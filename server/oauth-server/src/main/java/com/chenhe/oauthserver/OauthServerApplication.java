package com.chenhe.oauthserver;

import com.chenhe.oauthserver.service.OauthRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class OauthServerApplication {
    @Autowired
    RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate<String, Object> stringSerializerRedisTemplate() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        /*ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/

        OauthRedisSerializer customerRedisSerializer = new OauthRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(customerRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(customerRedisSerializer);

        return redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }
}
