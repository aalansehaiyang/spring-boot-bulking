package com.weiguanjishu.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */

@Slf4j
@Component
public class PulsarMessageClientConfig {

    @Value("${pulsar.service.url}")
    private String serviceUrl;

    private PulsarClient client;

    @PostConstruct
    void init() throws PulsarClientException {
        if (null == client) {
            client = PulsarClient.builder()
                    .serviceUrl(serviceUrl)
                    .listenerThreads(9)
                    .build();
        }
    }

    @PreDestroy
    void destroy() throws PulsarClientException {
        if (null != client) {
            client.close();
        }
    }

    public Producer createProducer(String topic, int delayMilliseconds,
                                   int timeoutSeconds, boolean blockIfQueueFull) throws PulsarClientException {
        return client.newProducer()
                .topic(topic)
                .batchingMaxPublishDelay(delayMilliseconds, TimeUnit.MILLISECONDS)
                .sendTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .blockIfQueueFull(blockIfQueueFull)
                .create();
    }

    public Producer createProducer(String topic) throws PulsarClientException {
        return createProducer(topic, 10, 10, true);
    }

    public Consumer<byte[]> createConsumer(String topic, String subscriptionName, MessageListener<byte[]> listener,
                                           int ackTimeout) throws PulsarClientException {
        return client.newConsumer()
                .topic(topic)
                .subscriptionName(subscriptionName)
                .ackTimeout(ackTimeout, TimeUnit.MINUTES)
                .subscriptionType(SubscriptionType.Shared)
                .messageListener(listener)
                .negativeAckRedeliveryDelay(5, TimeUnit.SECONDS)
                .subscribe();
    }

    public Consumer<byte[]> createConsumer(String topic, String subscriptionName, MessageListener<byte[]> listener) throws PulsarClientException {
        return createConsumer(topic, subscriptionName, listener, 10);
    }

}
