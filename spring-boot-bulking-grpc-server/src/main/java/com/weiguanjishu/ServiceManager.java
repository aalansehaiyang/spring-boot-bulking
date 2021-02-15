package com.weiguanjishu;


import com.weiguanjishu.service.HelloService;
import com.weiguanjishu.service.UserService;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @author 微信公众号：微观技术
 */

@Component
public class ServiceManager {

    private Server server;
    private int grpcServerPort = 9091;

    public void loadService(Map<String, Object> grpcServiceBeanMap) throws IOException, InterruptedException {

        ServerBuilder serverBuilder = ServerBuilder.forPort(grpcServerPort);
        // 采用注解扫描方式，添加服务
        for (Object bean : grpcServiceBeanMap.values()) {
            serverBuilder.addService((BindableService) bean);
            System.out.println(bean.getClass().getSimpleName() + " is regist in Spring Boot！");

        }
        server = serverBuilder.build().start();

        System.out.println("grpc server is started at " + grpcServerPort);

        // 增加一个钩子，当JVM进程退出时，Server 关闭
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                if (server != null) {
                    server.shutdown();
                }
                System.err.println("*** server shut down！！！！");
            }
        });

        server.awaitTermination();
    }
}
