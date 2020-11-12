package com.weiguanjishu.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 微信公众号：微观技术
 */

@Component
public class CacheService {

    private static String prefixKey = "weiguanjishu:";
    @Resource
    private RedisTemplate redisTemplate;

    public void delKey(String key) {
        redisTemplate.delete(key);
    }

    public boolean set(String key, String value) {
        try {
            key = prefixKey + key;
            redisTemplate.opsForValue().set(key, value, 30, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String get(String key) {
        key = prefixKey + key;
        return (String) redisTemplate.opsForValue().get(key);
    }


    public boolean setObject(String key, Object value) {
        try {
            key = prefixKey + key;
            redisTemplate.opsForValue().set(key, value, 30, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> T getObject(String key) {
        key = prefixKey + key;
        return (T) redisTemplate.opsForValue().get(key);
    }


    public boolean addData(String key, String value, double score) {
        boolean result = redisTemplate.opsForZSet().add(key, value, score);
        return result;
    }

    public List<String> scanData(String key, double max, long count) {
        Set<ZSetOperations.TypedTuple<String>> redisSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, 0, max, 0, count);
        return redisSet.stream().map(t -> t.getValue()).collect(Collectors.toList());
    }

    public long removeData(String key, String value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }
}