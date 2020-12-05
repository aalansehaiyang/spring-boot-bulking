package org.common.fastdfs.core;

import org.common.fastdfs.entity.ChunkMeta;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * Created by louxiu
 * <p>
 * 存储服务读取
 */

public interface FastDfsReader extends FastDfsOperate {

    // 获取内容
    String getContentByRemoteId() throws MalformedURLException;

    ChunkMeta getChunkMeta(String remoteId) throws Exception;

    String[] getHeaders() throws Exception;

    /**
     * 全量读取
     *
     * @return 记录列表
     */
    List<Map<String, Object>> readAll();

    Map<String, Object> readLine();

    List<Map<String, Object>> readBatch(int batchSize);

    void close();
}
