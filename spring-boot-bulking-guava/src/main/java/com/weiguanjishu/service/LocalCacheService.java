package com.weiguanjishu.service;


import com.google.common.cache.*;
import com.weiguanjishu.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class LocalCacheService {


    // 缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
    public static LoadingCache<Long, User> userCache
            = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(8)
            // 一定时间内没有创建/覆盖时（只认写动作），会移除该key，下次取的时候从loading中取
            .expireAfterWrite(5, TimeUnit.SECONDS)
            // 在一定时间内没有读写，会移除该key，下次取的时候从loading中取
            .expireAfterAccess(20, TimeUnit.SECONDS)
            // 设置写缓存后15秒钟刷新
            .refreshAfterWrite(3, TimeUnit.SECONDS)
            // 缓存容器的初始容量为5
            .initialCapacity(5)
            // 缓存最大容量为 10，超过之后就会按照LRU 移除缓存项
            .maximumSize(10)
            // 统计缓存的命中率，线上环境一般不需要
            .recordStats()
            //设置缓存的移除通知
            .removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> notification) {
                    System.out.println(notification.getKey() + " 被移除了，原因： " + notification.getCause());
                }
            })
            // 指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
            .build(
                    new CacheLoader<Long, User>() {
                        @Override
                        public User load(Long id) throws Exception {
                            System.out.println("缓存未命中，从数据库加载，用户id：" + id);
                            return User.builder().id(id).userName("Lily").age(new Random().nextInt(20)).build();
                        }
                    }
            );

}
