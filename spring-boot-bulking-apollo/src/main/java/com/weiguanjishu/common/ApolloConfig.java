package com.weiguanjishu.common;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 微信公众号：微观技术
 */


@Component
public class ApolloConfig {

    private static final String USER_TIMEOUT = "user.timeout";

    @PostConstruct
    public void init() {
        Config config = ConfigService.getAppConfig();
        config.addChangeListener(changeEvent -> {
            ConfigChange configChange = changeEvent.getChange(USER_TIMEOUT);
            PropertyChangeType changeType = configChange.getChangeType();
            if (PropertyChangeType.ADDED.equals(changeType) || PropertyChangeType.MODIFIED
                    .equals(changeType)) {
                System.out.println(String.format("动态刷新的新值。key:%s  ， 值：%s", USER_TIMEOUT, configChange.getNewValue()));
            }
        }, Sets.newHashSet(USER_TIMEOUT));

        String userTimeoutValue = config.getProperty(USER_TIMEOUT, null);
        System.out.println(String.format("首次拉取。key:%s  ， 值：%s", USER_TIMEOUT, userTimeoutValue));
    }
}
