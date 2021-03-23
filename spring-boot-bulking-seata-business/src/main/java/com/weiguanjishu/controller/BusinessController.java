package com.weiguanjishu.controller;

import com.weiguanjishu.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/business")
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
//https://blog.csdn.net/a88328734/article/details/108147670


public class BusinessController {

    private final BusinessService businessService;

    /**
     * 购买下单，模拟全局事务提交
     * <p>
     * http://127.0.0.1:8090/api/business/purchase/commit
     */
    @RequestMapping("/purchase/commit")
    public Boolean purchaseCommit(HttpServletRequest request) {
        businessService.purchase("101", "6666", 1);
        return true;
    }

    /**
     * 购买下单，模拟全局事务回滚
     * <p>
     * http://127.0.0.1:8090/api/business/purchase/rollback
     */
    @RequestMapping("/purchase/rollback")
    public Boolean purchaseRollback() {
        try {
            businessService.purchase("102", "6666", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}