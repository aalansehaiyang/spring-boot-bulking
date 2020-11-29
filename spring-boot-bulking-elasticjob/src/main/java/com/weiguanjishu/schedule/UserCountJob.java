package com.weiguanjishu.schedule;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 微信公众号：微观技术
 */

@Slf4j
@Component
public class UserCountJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {

        // 分片上下文参数
        System.out.println("ShardingContext：" + JSON.toJSONString(shardingContext));
        long userCount = 1200;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 用户注册数：" + userCount);
    }
}
