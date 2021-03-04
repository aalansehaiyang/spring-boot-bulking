package com.weiguanjishu.tool;

import org.springframework.util.StopWatch;

/**
 * @author 微信公众号：微观技术
 */

public class StopWatchCostTime {

    public static void main(String[] args) throws InterruptedException {

        StopWatch sw = new StopWatch();
        sw.start("任务1");
        // 模拟业务逻辑处理
        Thread.sleep(300);
        sw.stop();

        sw.start("任务2");
        // 模拟业务逻辑处理
        Thread.sleep(730);
        sw.stop();

        System.out.println(sw.prettyPrint());
    }
}
