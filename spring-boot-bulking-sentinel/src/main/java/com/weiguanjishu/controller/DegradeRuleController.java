package com.weiguanjishu.controller;


import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.weiguanjishu.common.TaskResourceType;
import com.weiguanjishu.service.SlowRatioCircuitBreakerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@RequestMapping("/rule")
@RestController
public class DegradeRuleController {

    @Resource
    private SlowRatioCircuitBreakerService slowRatioCircuitBreakerService;

    /**
     * http://127.0.0.1:8090/rule/update?minRequestAmount=1&slowRatioThreshold=1
     */
    @GetMapping(value = "/update")
    public String updateRule(@RequestParam Integer minRequestAmount, @RequestParam double slowRatioThreshold) {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule(TaskResourceType.task1)  //资源名，即规则的作用对象
                // 熔断策略，支持慢调用比例/异常比例/异常数策略
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                //慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
                .setCount(50)
                // 熔断时长，单位为 s
                .setTimeWindow(15)
                //熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
                .setMinRequestAmount(minRequestAmount)
                // 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
                .setSlowRatioThreshold(slowRatioThreshold)
                //统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
                .setStatIntervalMs(14_000);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        System.out.println("熔断规则加载: " + rules);

        return "提交成功";

    }

}

