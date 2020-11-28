package com.weiguanjishu.common;


import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 微信公众号：微观技术
 */

@Configuration
public class RabbitMQConfig {

    @Getter
    public static String NEW_USER_TOPIC = "new-user";

    /**
     * 自动创建队列
     */
    @Bean
    public Queue createQueue() {
        return new Queue(NEW_USER_TOPIC, true);
    }
}
