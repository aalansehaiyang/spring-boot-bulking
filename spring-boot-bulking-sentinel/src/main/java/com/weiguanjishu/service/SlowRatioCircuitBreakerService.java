package com.weiguanjishu.service;


import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SlowRatioCircuitBreakerService {

    public static Long startTime;
    private static AtomicInteger total = new AtomicInteger();
    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();


    public void createOrder(String taskResource, int num, Object object) {
        total.incrementAndGet();
        if (num == 1) {
            startTime = System.currentTimeMillis();
        }
        Entry entry = null;
        int handleTime = 0;
        try {
            entry = SphU.entry(taskResource);

            // RT: [40ms, 60ms)
            handleTime = ThreadLocalRandom.current().nextInt(40, 70);
            pass.incrementAndGet();
            System.out.println("任务【 " + num + " 】正常处理，耗时： " + handleTime + "  ！ 总统计量：total=" + total + " pass=" + pass);
            sleep(handleTime);
        } catch (BlockException e) {
            block.incrementAndGet();
            System.out.println("任务【 " + num + " 】 熔断，耗时： " + handleTime + "  ！ 总统计量：total=" + total + " block=" + block);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    private static void sleep(int timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
