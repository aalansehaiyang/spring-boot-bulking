package com.weiguanjishu.consumer;

import com.weiguanjishu.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * @author 微信公众号：微观技术
 */

@Component
public class UserPulsarMsgConsumer extends PulsarMsgConsumer<User> {

    @Override
    protected String getTopic() {
        return "new_user";
    }

    @Override
    protected String getSubscriptionName() {
        return "weiguanjishu_new_user_subscription";
    }

    @Override
    public void handleMsg(User user) {

        System.out.println("当前时间：" + System.currentTimeMillis() / 1000);
        System.out.println("消费消息：" + user);
    }
}
