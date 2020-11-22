package com.weiguanjishu.controller;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    private static String addUserTopic = "add_user";

    /**
     * 普通消息，带回调处理
     * http://localhost:8090/add_user1
     */
    @GetMapping("/add_user1")
    public Object add1() {
        try {
            Long id = Long.valueOf(new Random().nextInt(1000));
            User user = User.builder().id(id).userName("TomGE").age(29).address("上海").build();
            ListenableFuture<SendResult> listenableFuture = kafkaTemplate.send(addUserTopic, JSON.toJSONString(user));

            // 提供回调方法，可以监控消息的成功或失败的后续处理
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult>() {
                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println("发送消息失败，" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult sendResult) {
                    // 消息发送到的topic
                    String topic = sendResult.getRecordMetadata().topic();
                    // 消息发送到的分区
                    int partition = sendResult.getRecordMetadata().partition();
                    // 消息在分区内的offset
                    long offset = sendResult.getRecordMetadata().offset();
                    System.out.println(String.format("发送消息成功，topc：%s, partition: %s, offset：%s ", topic, partition, offset));
                }
            });
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }

    /**
     * 事务消息
     * <p>
     * http://localhost:8090/add_user2
     */
    @GetMapping("/add_user2")
    public Object add2() {
        try {
            Long id = Long.valueOf(new Random().nextInt(1000));
            User user = User.builder().id(id).userName("TomGE").age(29).address("上海").build();
            kafkaTemplate.executeInTransaction(op -> {
                op.send(addUserTopic, JSON.toJSONString(user));
                throw new RuntimeException("事务消息发送失败");
            });
            return "消息发送成功";

        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }
}
