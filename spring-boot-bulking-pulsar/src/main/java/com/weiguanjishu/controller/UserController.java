package com.weiguanjishu.controller;

import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.producer.UserPulsarMsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Autowired
    private UserPulsarMsgProducer userPulsarMsgProducer;

    /**
     * 普通消息发送
     * <p>
     * http://localhost:8090/add_user1
     */
    @GetMapping("/add_user1")
    public Object add1() {
        try {
            Long id = Long.valueOf(new Random().nextInt(1000));
            User user = User.builder().id(id).userName("TomGE").age(29).address("上海").build();
            userPulsarMsgProducer.send(user);
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }


    /**
     * 延迟消息发送
     * <p>
     * http://localhost:8090/add_user2
     */
    @GetMapping("/add_user2")
    public Object add2() {
        try {
            Long id = Long.valueOf(new Random().nextInt(1000));
            User user = User.builder().id(id).userName("TomGE").age(29).address("上海").build();
            userPulsarMsgProducer.sendDelay(user, 10, TimeUnit.SECONDS);
            return "消息发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "消息发送失败";
        }
    }


}
