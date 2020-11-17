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
     * 以较短的时间为准，过期掉对应的key
     * .expireAfterWrite(10, TimeUnit.SECONDS)
     * .expireAfterAccess(20, TimeUnit.SECONDS)
     */
    @Test
    public void test1_expireAfterWrite() throws ExecutionException, InterruptedException {
        User user1 = LocalCacheService.userCache.get(1L);
        System.out.println("第1次查user1：" + user1);

        // 模拟休眠 12秒
        Thread.sleep(12_000);

        User user2 = LocalCacheService.userCache.get(1L);
        System.out.println("第2次查user1：" + user2);
    }

    /**
     * 以较短的时间为准，过期掉对应的key
     * .expireAfterWrite(20, TimeUnit.SECONDS)
     * .expireAfterAccess(10, TimeUnit.SECONDS)
     */
    @Test
    public void test2_expireAfterWrite() throws ExecutionException, InterruptedException {
        User user1 = LocalCacheService.userCache.get(1L);
        System.out.println("第1次查user1：" + user1);
        user1 = LocalCacheService.userCache.get(1L);
        user1 = LocalCacheService.userCache.get(1L);

        // 模拟休眠
        Thread.sleep(13_000);

        User user2 = LocalCacheService.userCache.get(1L);
        System.out.println("第2次查user1：" + user2);
    }


    /**
     * 每隔3秒，缓存中的值被替换
     * .expireAfterWrite(5, TimeUnit.SECONDS)
     * .expireAfterAccess(20, TimeUnit.SECONDS)
     * .refreshAfterWrite(3, TimeUnit.SECONDS)
     */
    @Test
    public void test3_refreshAfterWrite() throws ExecutionException, InterruptedException {

        for (int i = 1; i < 5; i++) {
            User user1 = LocalCacheService.userCache.get(1L);
            System.out.println("第1次查user1：" + user1);

            // 模拟休眠
            Thread.sleep(2_000);
        }
    }

    /**
     * 容量上限是10，超过容量会LRU置换
     * maximumSize(10)
     */
    @Test
    public void test4_maximumSize() throws ExecutionException, InterruptedException {
        for (int i = 1; i < 16; i++) {
            User user1 = LocalCacheService.userCache.get(Long.valueOf(i));
            System.out.println(String.format("第%s次查user：%s", i, user1));
        }
    }

    @Test
    public void test5() throws ExecutionException, InterruptedException {
        User user1 = null;
        // 如果缓存中存在才返回
        user1 = LocalCacheService.userCache.getIfPresent(1L);
        System.out.println("查user1：" + user1);

        LocalCacheService.userCache.put(1L, User.builder().id(1L).userName("Tom").build());
        user1 = LocalCacheService.userCache.get(1L);
        System.out.println("查user1：" + user1);
    }
}


