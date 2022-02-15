package com.weiguanjishu.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ThreadPoolConfig {


    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor defaultPool() {
        return new ThreadPoolExecutor(3, 15, 5, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100)
        );
    }

}
