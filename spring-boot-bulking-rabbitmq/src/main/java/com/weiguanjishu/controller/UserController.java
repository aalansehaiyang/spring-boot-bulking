package com.weiguanjishu.controller;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.common.RabbitMQConfig;
import com.weiguanjishu.domain.model.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {


    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static String addUserTopic = "add_user";

    /**
     * 普通消息
     * http://localhost:9091/add_user
     */
    @GetMapping("/add_user")
    public Object add() {
        try {
            Long id = Long.valueOf(new Random().nextInt(1000));
            User user = User.builder().id(id).userName("TomGE").age(29).address("上海").build();
            byte[] useByte = JSON.toJSONString(user).getBytes(StandardCharsets.UTF_8);

            // 指定消息类型
            MessageProperties props = MessagePropertiesBuilder.newInstance()
                    .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build();

            rabbitTemplate.send(RabbitMQConfig.NEW_USER_TOPIC, new Message(useByte, props));

            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }

}
