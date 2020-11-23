package com.weiguanjishu.producer;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.common.PulsarMessageClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */

@Slf4j
public abstract class PulsarMsgProducer<T> implements MsgProducer<T> {

    @Resource
    private PulsarMessageClientConfig client;

    protected Producer<byte[]> producer;

    @PostConstruct
    public void init() throws PulsarClientException {
        String topic = getTopic();
        if (StringUtils.isEmpty(topic)) {
            throw new RuntimeException("topic cannot be null!");
        }
        producer = client.createProducer(getTopic());
    }

    @PreDestroy
    void destroy() throws PulsarClientException {
        if (null != producer) {
            producer.close();
        }
    }

    protected abstract String getTopic();

    @Override
    public void send(T msg) {
        String msgBody = JSON.toJSONString(msg);
        try {
            MessageId messageId = producer.send(msgBody.getBytes(StandardCharsets.UTF_8));
            log.info("pulsar msg send success, topic:{}, messageId:{}, msg:{}", getTopic(), messageId, msgBody);
        } catch (Throwable e) {
            log.error("pulsar msg send failed, topic:{}, msg:{}", getTopic(), msgBody);
        }
    }

    @Override
    public void sendDelay(T msg, long delay, TimeUnit unit) {

        String msgBody = JSON.toJSONString(msg);
        try {
            MessageId messageId = producer.newMessage().deliverAfter(delay, unit)
                    .value(msgBody.getBytes(StandardCharsets.UTF_8))
                    .send();
            log.info("pulsar msg sendDelay success, topic:{}, messageId:{}, msg:{}", getTopic(), messageId, msgBody);
        } catch (Exception e) {
            log.error("pulsar msg sendDelay failed, topic:{}, msg:{}", getTopic(), msgBody, e);
        }

    }


}
