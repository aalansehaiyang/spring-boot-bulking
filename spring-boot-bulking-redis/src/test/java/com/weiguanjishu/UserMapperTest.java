package com.weiguanjishu;


import com.weiguanjishu.cache.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class UserMapperTest {

    @Resource
    private CacheService cacheService;

    @Test
    public void valueSet() {
        cacheService.set("name", "微观技术");
    }

    @Test
    public void setV1() {
        String cacheResult = cacheService.get("name");
        System.out.println("name的缓存结果：" + cacheResult);
    }
}
