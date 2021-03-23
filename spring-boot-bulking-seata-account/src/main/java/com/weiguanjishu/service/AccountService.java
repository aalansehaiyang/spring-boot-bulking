package com.weiguanjishu.service;

import com.weiguanjishu.entity.Account;
import com.weiguanjishu.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AccountService {

    private static final String ERROR_USER_ID = "102";

    @Resource
    private AccountMapper accountMapper;

    public void debit(String userId, BigDecimal num) {
        Account account = accountMapper.selectByUserId(userId);
        account.setMoney(account.getMoney().subtract(num));
        accountMapper.updateById(account);

        if (ERROR_USER_ID.equals(userId)) {
            throw new RuntimeException("account branch exception");
        }
    }

}
