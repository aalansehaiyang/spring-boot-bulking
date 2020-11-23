package com.weiguanjishu.consumer;

/**
 * @author 微信公众号：微观技术
 */

public interface MsgConsumer<T> {

    /**
     * 消息处理逻辑
     */
    void handleMsg(T t);
}
