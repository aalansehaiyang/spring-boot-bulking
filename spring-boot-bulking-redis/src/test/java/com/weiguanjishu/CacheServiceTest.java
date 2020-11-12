package com.weiguanjishu;


import com.alibaba.fastjson.JSON;
import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.service.CacheService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CacheServiceTest {

    @Resource
    private CacheService cacheService;

    @Test
    public void test1_set() {
        boolean result = cacheService.set("k1", "微观技术");
        System.out.println(result);
    }

    @Test
    public void test2_get() {
        String cacheResult = cacheService.get("k1");
        System.out.println("k1 的缓存结果：" + cacheResult);
    }

    @Test
    public void test3_set() {
        User user = User.builder().id(1L).userName("雪糕").address("杭州").build();
        boolean result = cacheService.set("k2", JSON.toJSONString(user));
        System.out.println(result);
    }


    @Test
    public void test4_get() {
        String cacheResult = cacheService.get("k2");
        System.out.println("k2 的缓存结果：" + cacheResult);
    }

    @Test
    public void test5_setObject() {
        User user = User.builder().id(3L).userName("雪糕").address("杭州").age(2).build();
        boolean result = cacheService.setObject("k3", user);
        System.out.println(result);
    }


    @Test
    public void test6_getObject() {
        User user = cacheService.getObject("k3");
        System.out.println("k3 的缓存结果：" + user);
    }
}
