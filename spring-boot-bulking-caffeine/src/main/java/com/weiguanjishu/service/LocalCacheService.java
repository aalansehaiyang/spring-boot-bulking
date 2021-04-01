package com.weiguanjishu.service;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.weiguanjishu.domain.model.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */

@Service
public class LocalCacheService {


    public static LoadingCache<Long, User> loadingCache = Caffeine.newBuilder()
            // 初始的缓存空间大小
            .initialCapacity(5)
            // 缓存的最大条数
            .maximumSize(10)
            .expireAfterWrite(4, TimeUnit.SECONDS)
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .refreshAfterWrite(6, TimeUnit.SECONDS)
            .recordStats()
            //设置缓存的移除通知
            .removalListener(new RemovalListener<Long, User>() {
                @Override
                public void onRemoval(@Nullable Long key, @Nullable User user, @NonNull RemovalCause removalCause) {
                    System.out.printf("Key： %s ，值：%s was removed!原因 (%s) \n", key, user, removalCause);
                }
            })
            .build(id -> {
//                Thread.sleep(2_000);
                System.out.println("缓存未命中，从数据库加载，用户id：" + id);
                return User.builder().id(id).userName("Lily").age(new Random().nextInt(20)).build();
            });

}
