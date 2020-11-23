package com.weiguanjishu.producer;

import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */

public interface MsgProducer<T> {

    /**
     * 发送消息
     */
    void send(T msg);

    /**
     * 延时发送消息
     */
    void sendDelay(T msg, long delay, TimeUnit unit);
}
