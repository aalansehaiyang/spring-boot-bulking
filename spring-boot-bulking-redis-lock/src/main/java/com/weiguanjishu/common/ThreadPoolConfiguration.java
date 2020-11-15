package com.weiguanjishu.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 微信公众号：微观技术
 */

@Configuration
@Slf4j
public class ThreadPoolConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService coreRequestExecutor() {
        ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(20, 100, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5000),
                new ThreadFactoryBuilder().setNameFormat("core-executor-%d").build(),
                (r, executor) -> log.error("Core thread pool is full! "));
        taskExecutor.allowCoreThreadTimeOut(true);
        return taskExecutor;
    }
}
