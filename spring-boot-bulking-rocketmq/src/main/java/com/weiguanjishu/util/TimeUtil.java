package com.weiguanjishu.util;


/**
 * @author 微信公众号：微观技术
 */

public class TimeUtil {

    public static Long startTime;

    public static long costTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
