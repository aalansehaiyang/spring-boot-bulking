package com.weiguanjishu.annotation;

import com.example.grpc.HelloServiceGrpc;
import com.example.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 微信公众号：微观技术
 */

@Configuration
public class GrpcServiceConfig {

    @Bean
    public ManagedChannel getChannel() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        return channel;
    }

    @Bean
    public HelloServiceGrpc.HelloServiceBlockingStub getStub1(ManagedChannel channel) {
        return HelloServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub getStub2(ManagedChannel channel) {
        return UserServiceGrpc.newBlockingStub(channel);
    }
}
