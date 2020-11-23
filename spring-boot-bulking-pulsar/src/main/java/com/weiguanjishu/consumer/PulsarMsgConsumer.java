package com.weiguanjishu.consumer;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.common.PulsarMessageClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

/**
 * @author 微信公众号：微观技术
 */

@Slf4j
public abstract class PulsarMsgConsumer<T> implements MsgConsumer<T> {

    @Resource
    private PulsarMessageClientConfig client;

    private Consumer consumer;

    Class<T> clazzT = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

    @PostConstruct
    void init() throws PulsarClientException {
        if (StringUtils.isEmpty(getTopic())) {
            throw new RuntimeException("topic cannot be null!");
        }
        if (StringUtils.isEmpty(getSubscriptionName())) {
            throw new RuntimeException("subscriptionName cannot be null!");
        }
        consumer = client.createConsumer(getTopic(), getSubscriptionName(), new DefaultJsonMsgListener());
    }

    @PreDestroy
    void destroy() throws PulsarClientException {
        if (null != consumer) {
            consumer.close();
        }
    }

    protected abstract String getTopic();

    protected abstract String getSubscriptionName();


    class DefaultJsonMsgListener implements MessageListener<byte[]> {

        @Override
        public void received(Consumer<byte[]> consumer, Message<byte[]> message) {
            if (null != message && null != message.getData() && message.getData().length != 0) {
                String msgBody = new String(message.getValue(), StandardCharsets.UTF_8);

                log.warn("topic:{} receive message:{}", getTopic(), msgBody);
                try {
                    T msg = JSON.parseObject(msgBody, clazzT);
                    handleMsg(msg);
                } catch (Exception e) {
                    log.error("handle msg failed, topic:{}, message:{}", getTopic(), msgBody, e);
                    return;
                }
            }

            try {
                // 提交消费位移
                consumer.acknowledge(message);
            } catch (PulsarClientException e) {
                log.error("topic:{} ack failed", getTopic(), e);
            }
        }
    }

}
