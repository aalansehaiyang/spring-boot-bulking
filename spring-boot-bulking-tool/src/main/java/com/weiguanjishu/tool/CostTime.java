package com.weiguanjishu.tool;


/**
 * @author 微信公众号：微观技术
 */

public class CostTime {

    public static void main(String[] args) throws InterruptedException {

        long start1 = System.currentTimeMillis();
        // 模拟业务逻辑处理
        Thread.sleep(300);
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        // 模拟业务逻辑处理
        Thread.sleep(730);
        long end2 = System.currentTimeMillis();

        System.out.println("执行操作1，耗时:" + (end1 - start1));
        System.out.println("执行操作2，耗时:" + (end2 - start2));
    }
}
