package com.weiguanjishu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

/**
 * @author 微信公众号：微观技术
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
public class StartApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(StartApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
