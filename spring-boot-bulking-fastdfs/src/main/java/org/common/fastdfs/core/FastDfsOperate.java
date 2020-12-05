package org.common.fastdfs.core;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by louxiu
 * FastDfs 上传接口
 */

public interface FastDfsOperate {

    Pair<String, String> getGroupAndPathFromRemoteId(String remoteId);

}
