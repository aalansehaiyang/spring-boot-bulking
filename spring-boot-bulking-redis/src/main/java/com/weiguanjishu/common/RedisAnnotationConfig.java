package com.weiguanjishu.common;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 微信公众号：微观技术
 */

@Configuration
public class RedisAnnotationConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .prefixKeysWith("cache:user:")
                .disableCachingNullValues()
                .serializeKeysWith(keySerializationPair())
                .serializeValuesWith(valueSerializationPair());

        Map map = new HashMap<String, RedisCacheConfiguration>();
        map.put("user", redisCacheConfiguration);

        return RedisCacheManager.builder(factory)
                .withInitialCacheConfigurations(map).build();
    }

    private RedisSerializationContext.SerializationPair<String> keySerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    private RedisSerializationContext.SerializationPair<Object> valueSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
    }
}