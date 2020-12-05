package org.common.fastdfs.core;

import com.alibaba.fastjson.JSON;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.common.fastdfs.TrackerClient;
import org.common.fastdfs.entity.ChunkInfo;
import org.common.fastdfs.entity.ChunkMeta;
import org.common.fastdfs.StorageClient;
import org.common.fastdfs.TrackerServer;
import org.common.fastdfs.utils.FastDfsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by louxiu
 * <p>
 * 抽象fastDfs 类
 */
@Slf4j
abstract class AbstractFastDfsOperate implements FastDfsOperate {

    //    1024*1024*100  100M
    // 分块大小阈值
//    @Value("${fastdfs.chunk.size:104857600}")
    Long slickChunkSize;

    /**
     * 这个是每次上传多少，而不是一个文件的大小
     */
    static final int READ_CHUNK_SIZE = 1024 * 1024;


    public static final String CHUNK_FLAG = "CHUNK_FLAG";


    // 下载的路径 要支持动态配置
    int port;

    /**
     * client
     */
    StorageClient storageClient;

    /**
     * tracker 地址
     */
    String trackers;


    /**
     * 存储专用，需要用到slickChunkSize 来进行存储切片
     *
     * @param trackers       tracekers 地址
     * @param slickChunkSize 块存储大小
     * @throws IOException
     */
    AbstractFastDfsOperate(String trackers, Long slickChunkSize, int port) throws IOException {
        this.trackers = trackers;
        this.port = port;
        this.slickChunkSize = slickChunkSize;
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        this.storageClient = new StorageClient(trackerServer, null);
    }

    /**
     * 读取专用，
     *
     * @param trackers tracekers 地址
     * @throws IOException
     */
    AbstractFastDfsOperate(String trackers, int port) throws IOException {
        this.trackers = trackers;
        this.port = port;
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        this.storageClient = new StorageClient(trackerServer, null);
    }

    /**
     * 删除/下载
     *
     * @param trackers tracekers 地址
     * @throws IOException
     */
    AbstractFastDfsOperate(String trackers) throws IOException {
        this.trackers = trackers;
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        this.storageClient = new StorageClient(trackerServer, null);
    }


    /**
     * 从文件路径中找到文件后缀
     *
     * @param filename 文件名称
     * @return 后缀名
     */
    String deduceExtName(String filename) {
        int pos = filename.lastIndexOf('.');
        if (pos > 0) {
            return filename.substring(pos + 1);
        }
        return null;
    }


    /**
     * 判断是否是分块
     *
     * @param remoteId
     * @return
     * @throws IOException
     */
    public boolean isSliceMeta(String remoteId) throws IOException {
        String url = getTrackerIpAndPort() + remoteId;

        byte[] meta;
        try (InputStream inputStream = new URL(url).openStream()) {
            meta = new byte[10];

            int read = inputStream.read(meta);
        }

        String s = new String(meta);
        return CHUNK_FLAG.equals(s);
    }


    /**
     * 前置判断是分块存储的，获取对应的chunkMeta
     *
     * @param remoteId
     * @return
     * @throws IOException
     */
    public ChunkMeta getChunkMeta(String remoteId) throws IOException {

        ChunkMeta chunkMeta = null;

        if (isSliceMeta(remoteId)) {

            String content = getContentByRemoteId(remoteId);

            content = content.replace(CHUNK_FLAG, "");

            chunkMeta = JSON.parseObject(content, ChunkMeta.class);
        }
        return chunkMeta;
    }


    /**
     * 获取chunkMeta InputStream
     *
     * @param remoteId fastdfs remoteId
     * @return SequenceInputStream
     * @throws IOException
     */
    public InputStream getInputStream(String remoteId) throws IOException {
        ChunkMeta chunkMeta = getChunkMeta(remoteId);

        Vector<String> inputStreamVector = new Vector<>();

        if (chunkMeta == null) {
            log.info("remoteId:{} 没有找到chunkMeta", remoteId);
            String url = getTrackerIpAndPort() + remoteId;
            inputStreamVector.add(url);
        } else {
            log.info("remoteId:{} 找到chunkMeta:{}", remoteId, chunkMeta);
            Long chunkCount = chunkMeta.getChunkCount();
            for (int i = 0; i < chunkCount; i++) {
                ChunkInfo chunkInfo = chunkMeta.getChunkInfoMap().get(String.valueOf(i));
                if (null == chunkInfo) {
                    log.info("[{}] 获取对应chunkinfo 为null index:{}", remoteId, i);
                    throw new IOException("获取某部分分片chuninfo失败");
                }
                log.info("remoteI {} chunkInfo:{}", remoteId, chunkInfo);
                String url = getTrackerIpAndPort() + chunkInfo.getChunkLink();
                inputStreamVector.add(url);
            }
        }

        return new CustomSequenceInputStream(inputStreamVector.elements());


    }


    /**
     * 根据remoteId获取数据
     *
     * @param remoteId
     * @return
     */
    public String getContentByRemoteId(String remoteId) {

        Pair<String, String> groupAndPathFromRemoteId = getGroupAndPathFromRemoteId(remoteId);
        String groupName = groupAndPathFromRemoteId.getKey();
        String fileName = groupAndPathFromRemoteId.getValue();
        byte[] download = this.download(groupName, fileName);
        if (null == download) {
            log.info("getContentByRemoteId: {} 下载失败", remoteId);
            return null;
        }
        return new String(download);

    }

    /**
     * 注意这里的数据只能下载很小的文件 几百k，
     * 如果有需要下载大文件到内存中，麻烦找louxiu重新写接口
     *
     * @param groupName
     * @param filename
     * @return
     */
    public byte[] download(String groupName, String filename) {

        try {
            byte[] bytes = storageClient.download_file(groupName, filename);
            return bytes;
        } catch (Exception e) {
            log.error("读取文件失败, groupName: {}, filename: {}", groupName, filename, e);
            return null;
        }

    }

    public String getTrackerIpAndPort() {
        return FastDfsUtils.getTrackerIpAndPort(trackers, port);
    }

    /**
     * 从 url 中找出实际的remoteId
     * http://www.baidu.com/dsal/ds/ds/ds/ --> dsal/ds/ds/ds/
     *
     * @param url
     * @return
     */
    public String getRempteIdFromUrl(String url) throws MalformedURLException {
        if (url.startsWith("http")) {
            String path = new URL(url).getPath();
            if (path.startsWith("/")) {
                return path.substring(1);
            } else {
                return path;
            }
        } else {
            return url;
        }
    }

    /**
     * 根据remoteId 获取group path
     *
     * @param remoteId
     * @return
     */
    @Override
    public Pair<String, String> getGroupAndPathFromRemoteId(String remoteId) {
        int poi = StringUtils.indexOf(remoteId, "/");
        String groupName = StringUtils.substring(remoteId, 0, poi);
        String fileName = StringUtils.substring(remoteId, poi + 1, remoteId.length());
        return new Pair<>(groupName, fileName);
    }

}
