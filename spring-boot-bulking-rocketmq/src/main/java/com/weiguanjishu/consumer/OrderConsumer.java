package com.weiguanjishu.consumer;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.model.OrderModel;
import com.weiguanjishu.util.TimeUtil;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author 微信公众号：微观技术
 */
@Service
@RocketMQMessageListener(nameServer = "${rocketmq.name-server}", topic = "${rocketmq.consumer.topic}", consumerGroup = "${rocketmq.consumer.group}")
public class OrderConsumer implements RocketMQListener<OrderModel> {

    @Override
    public void onMessage(OrderModel orderModel) {
        System.out.printf("consumer received message: %s \n", JSON.toJSONString(orderModel));
        System.out.println("距离消息发送过去：" + (System.currentTimeMillis() - orderModel.getStartTime()) / 1000 + "秒");
    }
}