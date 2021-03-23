package com.weiguanjishu.service;

import com.weiguanjishu.client.AccountClient;
import com.weiguanjishu.entity.Order;
import com.weiguanjishu.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderService {

    @Autowired
    private AccountClient accountClient;

    @Resource
    private OrderMapper orderMapper;

    public void create(String userId, String commodityCode, Integer count) {
        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(10));
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(orderMoney);

        accountClient.debit(userId, orderMoney);
        orderMapper.insert(order);


    }
}