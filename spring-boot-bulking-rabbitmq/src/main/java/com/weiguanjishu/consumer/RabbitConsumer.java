package com.weiguanjishu.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author 微信公众号：微观技术
 */

@Component
public class RabbitConsumer {

    @RabbitListener(queues = "new-user")
    public void onMessage(Message message, Channel channel) throws Exception {
        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("消费内容 : " + s);
        // 提交消息ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
