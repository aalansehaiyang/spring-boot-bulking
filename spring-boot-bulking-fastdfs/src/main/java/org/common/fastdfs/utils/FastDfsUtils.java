package org.common.fastdfs.utils;

public class FastDfsUtils {

    // 获取ip以及port
    public static String getTrackerIpAndPort(String trackers, int port) {
        return "http://" + getTrackerIp(trackers) + ":" + port + "/";
    }

    private static String getTrackerIp(String trackers) {
        String[] split = trackers.split(",");
        String tracker = split[0];
        String[] split1 = tracker.split(":");
        return split1[0];
    }
}
