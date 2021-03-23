package com.weiguanjishu.controller;

import com.weiguanjishu.service.AccountService;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 地址：http://127.0.0.1:8081/account/debit?userId=101&orderMoney=5
     */
    @RequestMapping("/debit")
    public Object debit(@RequestParam String userId, @RequestParam BigDecimal orderMoney) {
        System.out.println("account XID： " + RootContext.getXID());
        accountService.debit(userId, orderMoney);
        return "扣减成功，count=" + orderMoney;
    }

}
