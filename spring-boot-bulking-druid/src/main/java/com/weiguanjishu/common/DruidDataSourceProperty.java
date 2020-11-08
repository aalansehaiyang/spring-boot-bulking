package com.weiguanjishu.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 微信公众号：微观技术
 */

@Component
@Data
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidDataSourceProperty {
    // 数据库配置
    private String url;

    private String username;

    private String password;

    private String driverClassName;

    // 初始化大小，最小，最大
    private int initialSize = 0;

    private int minIdle;

    private int maxActive = 8;

    // 配置获取连接等待超时的时间
    private int maxWait;

    // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private int timeBetweenEvictionRunsMillis = 1000 * 60;

    // 配置一个连接在池中最小生存的时间，单位是毫秒
    private int minEvictableIdleTimeMillis = 1000 * 60 * 30;

    // 检测连接是否有效的sql
    private String validationQuery;

    private boolean testWhileIdle = false;

    private boolean testOnBorrow = true;

    private boolean testOnReturn = false;

    // PSCache Mysql下建议关闭
    private boolean poolPreparedStatements = false;

    private int maxPoolPreparedStatementPerConnectionSize = -1;

    // 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    private String filters;

    // 合并多个DruidDataSource的监控数据
    private boolean useGlobalDataSourceStat = false;

    private String connectionProperties;

}
