package org.common.fastdfs.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by louxiu
 * <p>
 * Fastdfs 下载接口
 */

public interface FastDfsDownload extends FastDfsOperate {

    /**
     * 根据remoteId 下载byte型数据
     * <p>
     * 尽量不要下载很大的 byte数组最大为2^32-1. 也就是4G. 超过oom/或者是被截断
     *
     * @param remoteId 组名+文件名
     * @return 二进制的文件
     */
    byte[] downloadByRemoteId(String remoteId) throws IOException;

    /**
     * 根据远程地址下载到本地temoPath
     *
     * @param tempPath      本地路径.
     * @param urlOrRemoteId fastDfs地址. (带有服务器地址)
     */
    void downloadToLocalTempPath(String tempPath, String urlOrRemoteId) throws Exception;

    /**
     * 获取存储服务的输出流
     *
     * @param remoteId
     * @return
     * @throws Exception
     */
    InputStream getInputStreamByRemoteId(String remoteId) throws IOException;

}
