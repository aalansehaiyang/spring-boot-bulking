package com.weiguanjishu.controller;

import com.weiguanjishu.service.OrderService;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * http://127.0.0.1:8082/api/order/debit?userId=101&commodityCode=6666&&count=1
     */
    @GetMapping(value = "/debit")
    public void debit(@RequestParam String userId, @RequestParam String commodityCode, @RequestParam Integer count) {
        System.out.println("order XID " + RootContext.getXID());
        orderService.create(userId, commodityCode, count);
    }

}
