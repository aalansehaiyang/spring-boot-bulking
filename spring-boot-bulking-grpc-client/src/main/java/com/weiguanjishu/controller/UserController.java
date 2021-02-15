package com.weiguanjishu.controller;

import com.example.grpc.*;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Resource
    HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    @Resource
    UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    /**
     * http://localhost:8098/hello
     */
    @SneakyThrows
    @RequestMapping("/hello")
    public String hello() {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Tom哥")
                .setAge(18)
                .addHobbies("read")
                .addHobbies("sing")
                .putTags("sex", "male")
                .build();
        HelloResponse helloResponse = helloServiceBlockingStub.hello(request);
        String result =helloResponse.getGreeting();
        System.out.println(result);
        return result;
    }

    /**
     * http://localhost:8098/query
     */
    @RequestMapping("/query")
    public String query() {
        UserRequest request = UserRequest.newBuilder()
                .setName("微观技术")
                .build();
        UserResponse userResponse = userServiceBlockingStub.query(request);
        String result = String.format("name:%s  , age:%s , address:%s ", userResponse.getName(), userResponse.getAge(), userResponse.getAddress());
        System.out.println(result);
        return result;
    }


}
