package com.weiguanjishu;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.weiguanjishu.annotation.GrpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Map;

/**
 * @author 微信公众号：微观技术
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
@EnableDubbo
public class StartApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(StartApplication.class, args);
    }
}
