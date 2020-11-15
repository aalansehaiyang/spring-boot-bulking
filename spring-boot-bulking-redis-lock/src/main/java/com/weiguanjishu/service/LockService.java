package com.weiguanjishu.service;

import org.assertj.core.util.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */

@Service
public class LockService {

    private static String prefixKey = "lock:";
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取锁
     * <p>
     * true：成功获取锁
     * false：本次请求没有拿到锁
     */
    public boolean lock(String key, String value, long expireTime) {
        key = prefixKey + key;

        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            lock = false;
        }

        if (lock) {
            System.out.println(String.format("%s 已经拿到了锁，当前时间：%s", Thread.currentThread().getName(), System.currentTimeMillis() / 1000));
        }
        return lock;
    }


    /**
     * 释放锁
     * <p>
     * true：成功释放
     * false：释放锁释放，也有可能是自动过期
     */
    public boolean unLock(String key, String value) {
        key = prefixKey + key;
        Long result = -1L;
        String luaScript =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                 "  return redis.call('del', KEYS[1]) " +
                 "else " +
                 "  return 0 " +
                 "end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript(luaScript, Long.class);
        try {
            // del 成功返回 1
            result = (Long) redisTemplate.execute(redisScript, Lists.list(key), value);
            // System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (result == 1) {
            System.out.println(String.format("%s 已经释放了锁，当前时间：%s", Thread.currentThread().getName(), System.currentTimeMillis() / 1000));
        }

        return result == 1 ? true : false;
    }
}
