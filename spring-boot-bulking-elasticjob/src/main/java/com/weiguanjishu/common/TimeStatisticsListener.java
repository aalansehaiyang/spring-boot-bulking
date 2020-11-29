package com.weiguanjishu.common;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 微信公众号：微观技术
 */

@Slf4j
public class TimeStatisticsListener implements ElasticJobListener {

    private Long start;

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        start = System.currentTimeMillis();
        log.info("{} job start.", shardingContexts.getJobName());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        log.info("{} job end. total cost {}ms.", shardingContexts.getJobName(), System.currentTimeMillis() - start);
    }
}
