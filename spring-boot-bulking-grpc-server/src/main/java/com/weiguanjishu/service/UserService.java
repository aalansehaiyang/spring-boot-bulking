package com.weiguanjishu.service;

import com.example.grpc.*;
import com.weiguanjishu.annotation.GrpcService;
import io.grpc.stub.StreamObserver;


/**
 * @author 微信公众号：微观技术
 */

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void query(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println(" UserService 接收到的参数，name：" + request.getName());

        UserResponse response = UserResponse.newBuilder().setName("微观技术").setAge(30).setAddress("上海").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
