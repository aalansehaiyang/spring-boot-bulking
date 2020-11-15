package com.weiguanjishu;

import com.weiguanjishu.service.LockService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultithreadTest {


    @Resource
    private LockService distributeLockService;

    @Resource
    private ThreadPoolExecutor coreRequestExecutor;

    /**
     * 多线程模拟锁抢占
     */
    @Test
    public void test1() throws InterruptedException {

        for (int i = 1; i < 11; i++) {
            coreRequestExecutor.execute(new Task());
        }

        Thread.sleep(300000);
    }

    class Task implements Runnable {

        @Override
        public void run() {
            String key = "key2";
            String value = UUID.randomUUID().toString();

            // 抢占锁
            boolean lock = distributeLockService.lock(key, value, 2_000);

            // 如果没有拿到锁，每500ms无限循环一次
            while (!lock) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock = distributeLockService.lock(key, value, 2_000);
            }

            //模拟业务处理
            try {
                Thread.sleep(new Random().nextInt(6) * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 释放锁
            distributeLockService.unLock(key, value);

        }
    }
}
