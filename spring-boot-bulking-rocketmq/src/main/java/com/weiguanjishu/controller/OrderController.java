package com.weiguanjishu.controller;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.model.OrderModel;
import com.weiguanjishu.util.TimeUtil;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
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
     * 同步发送
     * http://localhost:9071/send_make_order_message
     */
    @GetMapping("/send_make_order_message")
    public Object send_make_order_message() {
        try {
            OrderModel orderModel = mockOrderModel();
            SendResult sendResult = rocketMQTemplate.syncSend(makerOrderTopic, orderModel);
            System.out.printf("Send message to topic %s , sendResult=%s %n", makerOrderTopic, sendResult);

            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }

    /**
     * 同步发送
     * http://localhost:9071/sync_send
     */
    @GetMapping("/sync_send")
    public Object syncSend() {
        try {
            OrderModel orderModel = mockOrderModel();
            Message message = new Message(makerOrderTopic, "TageA", JSON.toJSONString(orderModel).getBytes());
            SendResult sendResult = rocketMQTemplate.getProducer().send(message);
            System.out.printf("Send message to topic %s , sendResult=%s %n", makerOrderTopic, sendResult);

            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }

    /**
     * 异步发送
     * http://localhost:9071/async_send
     */
    @GetMapping("/async_send")
    public Object asyncSend() {
        try {
            OrderModel orderModel = mockOrderModel();
            rocketMQTemplate.asyncSend(makerOrderTopic, orderModel, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("消息发送成功，msgId=" + sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送失败，" + throwable);
                }
            });
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }

    /**
     * 顺序发送
     * http://localhost:9071/sync_send_orderly
     */
    @GetMapping("/sync_send_orderly")
    public Object syncSendOrderly() {
        try {
            for (long orderId = 0; orderId < 20; orderId++) {
                String shardingKey = String.valueOf(orderId % 5);
                OrderModel orderModel = OrderModel.builder().orderId(orderId).build();
                SendResult sendResult = rocketMQTemplate.syncSendOrderly(makerOrderTopic, orderModel, shardingKey);
                if (sendResult != null) {
                    System.out.println(orderId + " ，发送成功");
                }
            }
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }


    /**
     * 延迟发送
     * http://localhost:9071/sync_send_delay
     */
    @GetMapping("/sync_send_delay")
    public Object syncSendDelay() {
        try {
            OrderModel orderModel = mockOrderModel();
            org.springframework.messaging.Message message = MessageBuilder.withPayload(JSON.toJSONString(orderModel).getBytes()).build();
            //延时等级 3, 这个消息将在10s之后发送，现在只支持固定的几个时间值
            //delayTimeLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
            SendResult sendResult = rocketMQTemplate.syncSend(makerOrderTopic, message, 8000, 3);
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }

    /**
     * 延迟发送
     * http://localhost:9071/send_message_transaction
     */
    @GetMapping("/send_message_transaction")
    public Object sendMessageInTransaction() {
        try {
            OrderModel orderModel = mockOrderModel();
            org.springframework.messaging.Message message = MessageBuilder.withPayload(JSON.toJSONString(orderModel)).build();
            TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("tx_order_message", makerOrderTopic, message, null);
            SendStatus sendStatus = transactionSendResult.getSendStatus();
            LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
            System.out.println("send message status： " + sendStatus + " ,  localTransactionState： " + localTransactionState);
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }


    public OrderModel mockOrderModel() {
        Long orderId = Long.valueOf(new Random().nextInt(1000000));
        OrderModel orderModel = OrderModel.builder()
                .orderId(orderId)
                .buyerUid(200000L)
                .amount(26.8)
                .shippingAddress("上海")
                .startTime(System.currentTimeMillis())
                .build();
        return orderModel;
    }


}
