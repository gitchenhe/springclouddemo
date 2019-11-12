package com.chenhe.oauthserver.service;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.SerializationUtils;

/**
 * @author chenhe
 * @date 2019-11-08 16:07
 * @desc oauth 序列化,序列化工具
 * 解决{@link com.chenhe.oauthserver.service.AuthCodeRedisSaveService} 的store方法的参数不能为反序列化的问题
 */
public class OauthRedisSerializer<T> implements RedisSerializer<T> {
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (null == o) {
            return null;
        }
        return SerializationUtils.serialize(o);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes) {
            return null;
        }
        return (T) SerializationUtils.deserialize(bytes);
    }
}
