package org.common.fastdfs.core;

import lombok.extern.slf4j.Slf4j;
import org.common.fastdfs.utils.FastDfsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by louxiu
 * <p>
 * fastdfs 上传中间件
 * <p>
 * 1 上传
 * - 单个
 * - 批量
 * 2 下载
 * - 单个
 * - 批量
 * 3 删除
 * - 单个
 * - 批量
 */
@Slf4j
@Component
public class FastDfsServiceImpl implements FastDfsService {


    @Value("${fastdfs.tracker.ng_module.port:8888}")
    private int port;

    @Value("${fastdfs.tracker.servers}")
    private String trackers;

    // 默认 10*1024 * 1024 = 10M 每一块存储最大大小
    @Value("${fastdfs.tracker.servers.chunksize:10485760}")
    private Long chunkSize;


    @Override
    public FastDfsReader getReader(String remoteIdOrUrl, FastDfsSliceReader.ProtocolEnum protocolEnum) throws IOException {
        return new FastDfsSliceReader(trackers, remoteIdOrUrl, protocolEnum, port);
    }

    @Override
    public FastDfsDownload getDownloader() throws IOException {
        return new FastDfsDownloadImpl(trackers, port);
    }

    @Override
    public FastDfsDelete getDeleter() throws IOException {
        return new FastDfsDeleteImpl(trackers, port);
    }

    @Override
    public FastDfsUpload getUploader() throws IOException {
        return new FastDfsUploadSliceImpl(trackers, chunkSize, port);
    }

    @Override
    public String getTrackerIpAndPort() {
        return FastDfsUtils.getTrackerIpAndPort(trackers, port);
    }
}
