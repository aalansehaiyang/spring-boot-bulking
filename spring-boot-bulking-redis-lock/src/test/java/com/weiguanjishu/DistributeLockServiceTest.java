package com.weiguanjishu;


import com.weiguanjishu.service.LockService;
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
public class DistributeLockServiceTest {

    @Resource
    private LockService distributeLockService;

    @Test
    public void test1_lock() {
        boolean result = distributeLockService.lock("key1", "weiguanjishu", 30_000);
        System.out.println("获取锁：" + result);
    }

    @Test
    public void test2_unLock() {
        boolean result = distributeLockService.unLock("key1", "weiguanjishu");
        System.out.println("释放锁：" + result);
    }


}
