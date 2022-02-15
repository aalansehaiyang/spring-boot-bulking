package com.weiguanjishu.common;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DegradeRuleConfig {


    @PostConstruct
    public static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule1 = new DegradeRule(TaskResourceType.task1)  //资源名，即规则的作用对象
                // 熔断策略，支持慢调用比例/异常比例/异常数策略
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                //慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
                .setCount(50)
                // 熔断时长，单位为 s
                .setTimeWindow(12)
                // 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
                .setSlowRatioThreshold(0.6)
                //熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
                .setMinRequestAmount(6)
                //统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
                .setStatIntervalMs(5000);
        rules.add(rule1);

        DegradeRule rule2 = new DegradeRule(TaskResourceType.task2)  //资源名，即规则的作用对象
                // 熔断策略，支持慢调用比例/异常比例/异常数策略
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                //慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
                .setCount(48)
                // 熔断时长，单位为 s
                .setTimeWindow(12)
                // 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
                .setSlowRatioThreshold(0.6)
                //熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
                .setMinRequestAmount(10)
                //统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
                .setStatIntervalMs(5000);
        rules.add(rule2);

        DegradeRuleManager.loadRules(rules);
        System.out.println("熔断规则加载: " + rules);


        // 设置状态监听器
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) -> {
                    if (newState == CircuitBreaker.State.OPEN) {
                        System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue));
                    } else {
                        System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(),
                                TimeUtil.currentTimeMillis()));
                    }
                });
    }
}
