package com.weiguanjishu.controller;

import com.weiguanjishu.model.OrderModel;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class OrderController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private static String makerOrderTopic = "maker-order-topic";


    /**
     * 普通消息
     * http://localhost:9071/send_make_order_message
     */
    @GetMapping("/send_make_order_message")
    public Object send_make_order_message() {
        try {
            Long orderId = Long.valueOf(new Random().nextInt(1000000));
            OrderModel orderModel = OrderModel.builder().orderId(orderId).buyerUid(200000L).amount(26.8).shippingAddress("上海").build();

            SendResult sendResult = rocketMQTemplate.syncSend(makerOrderTopic, orderModel);
            System.out.printf("Send message to topic %s , sendResult=%s %n", makerOrderTopic, sendResult);

            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }


}
