package com.weiguanjishu.common;


import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.weiguanjishu.schedule.UserCountJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 微信公众号：微观技术
 */

@Component
public class ElasticjobConfig {

    @Value("${elasticjob.zk}")
    private String zk;

    @Value("${elasticjob.namespace}")
    private String nameSpace;

    @Bean
    public CoordinatorRegistryCenter regCenter() {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(zk, nameSpace));
        regCenter.init();
        return regCenter;
    }

    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron) {
        return LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration
                        .newBuilder(jobClass.getSimpleName(), cron, 1)
                        .shardingItemParameters("1=1")
                        .build(), jobClass.getCanonicalName()))
                .overwrite(true)
                .build();
    }


    @Bean(initMethod = "init")
    public JobScheduler createBrandActivityJobSchedule(final UserCountJob userCountJob, CoordinatorRegistryCenter regCenter) {
        return new SpringJobScheduler(userCountJob, regCenter,
                getLiteJobConfiguration(userCountJob.getClass(),
                        // 每10秒执行一次
                        "*/10 * * * * ? *"), new TimeStatisticsListener());
    }


}
