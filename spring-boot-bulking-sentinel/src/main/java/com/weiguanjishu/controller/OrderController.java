package com.weiguanjishu.controller;

import com.weiguanjishu.service.SlowRatioCircuitBreakerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@RequestMapping("/order")
@RestController
public class OrderController {


    @Resource
    private SlowRatioCircuitBreakerService slowRatioCircuitBreakerService;

    @Resource(name = "defaultPool")
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * http://127.0.0.1:8090/order/create?begin=1&end=1&&taskResource=task1
     * http://127.0.0.1:8090/order/create?begin=1&end=1&&taskResource=task2
     */
    @GetMapping(value = "/create")
    public String createOrder(@RequestParam String taskResource, @RequestParam Integer begin, @RequestParam Integer end) {

        for (int num = begin; num <= end; num++) {
            int finalNum = num;
            threadPoolExecutor.submit(() -> {
                slowRatioCircuitBreakerService.createOrder(taskResource, finalNum, null);
            });
        }

        return "提交成功";

    }

}
