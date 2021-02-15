package com.weiguanjishu.service;

import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;
import com.example.grpc.HelloServiceGrpc;
import com.weiguanjishu.annotation.GrpcService;
import io.grpc.stub.StreamObserver;


/**
 * @author 微信公众号：微观技术
 */

@GrpcService
public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println(" HelloService 接收到的参数，name：" + request.getName());

        String greeting = "Hi " + request.getName() + " ,you are " + request.getAge() + " years old" +
                " ,your hoby is " + (request.getHobbiesList()) + " ,your tags " + request.getTagsMap();

        HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}