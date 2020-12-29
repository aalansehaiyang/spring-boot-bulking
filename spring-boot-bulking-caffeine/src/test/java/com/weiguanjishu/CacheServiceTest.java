package com.weiguanjishu;

import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.service.LocalCacheService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CacheServiceTest {


    /**
     * .refreshAfterWrite(6,TimeUnit.SECONDS)
     * .build(id -> {
     *      Thread.sleep(2_000);
     *      System.out.println("缓存未命中，从数据库加载，用户id：" + id);
     *      return User.builder().id(id).userName("Lily").age(new Random().nextInt(20)).build();
     * });
     */
    @Test
    public void test1_refreshAfterWrite() throws ExecutionException, InterruptedException {
        User user1 = LocalCacheService.loadingCache.get(1L);
        System.out.println("第1次查user1：" + user1);

        Thread.sleep(10_000);

        // 缓存不存在，返回旧值，并触发缓存刷新
        user1 = LocalCacheService.loadingCache.get(1L);
        System.out.println("第2次查user1：" + user1);

        Thread.sleep(5_000);

        user1 = LocalCacheService.loadingCache.get(1L);
        System.out.println("第3次查user1：" + user1);
    }

    /**
     * .expireAfterWrite(4, TimeUnit.SECONDS)
     * .expireAfterAccess(10,TimeUnit.SECONDS)
     */
    @Test
    public void test2() throws ExecutionException, InterruptedException {
        User user1 = LocalCacheService.loadingCache.get(1L);
        System.out.println("第1次查user1：" + user1);

        Thread.sleep(6_000);

        user1 = LocalCacheService.loadingCache.get(1L);
        System.out.println("第2次查user1：" + user1);
    }

    /**
     * 容量上限是10，超过容量会LRU置换
     * maximumSize(10)
     */
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        for (int i = 1; i < 16; i++) {
            User user1 = LocalCacheService.loadingCache.get(Long.valueOf(i));
            System.out.println(String.format("第%s次查user：%s", i, user1));
        }

        Thread.sleep(30_000);
    }

}


