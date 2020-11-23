package com.weiguanjishu.producer;


import com.weiguanjishu.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * @author 微信公众号：微观技术
 */

@Component
public class UserPulsarMsgProducer extends PulsarMsgProducer<User> {

    @Override
    protected String getTopic() {
        return "new_user";
    }
}
