package com.chenhe.oauthserver.service;

import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.SerializationUtils;

import java.nio.charset.Charset;

/**
 * @author chenhe
 * @date 2019-11-08 17:19
 * @desc
 */
public class OauthRedisTokenStoreSerializationStrategy implements RedisTokenStoreSerializationStrategy {
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (null == bytes){
            return null;
        }
        return (T) SerializationUtils.deserialize(bytes);
    }

    @Override
    public String deserializeString(byte[] bytes) {
        if (null == bytes){
            return null;
        }
        return new String(bytes,Charset.forName("utf-8"));
    }

    @Override
    public byte[] serialize(Object object) {
        if (null == object) {
            return null;
        }
        return SerializationUtils.serialize(object);
    }

    @Override
    public byte[] serialize(String data) {
        if (null == data) {
            return null;
        }
        return data.getBytes(Charset.forName("utf-8"));
    }
}
