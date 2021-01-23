package com.weiguanjishu.consumer;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.model.OrderModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author onlyone
 * @create 2021/1/22
 */

@Service
@RocketMQMessageListener(nameServer = "${rocketmq.name-server}", topic = "${rocketmq.consumer.topic}", consumerGroup = "${rocketmq.consumer.group}")
public class OrderConsumer implements RocketMQListener<OrderModel> {

    @Override
    public void onMessage(OrderModel orderModel) {
        System.out.printf("consumer received message: %s \n", JSON.toJSONString(orderModel));
    }
}