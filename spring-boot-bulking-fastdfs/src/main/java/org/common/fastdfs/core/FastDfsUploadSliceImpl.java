package org.common.fastdfs.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.common.fastdfs.entity.ChunkInfo;
import org.common.fastdfs.entity.ChunkMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by louxiu
 */
@Slf4j
public class FastDfsUploadSliceImpl extends AbstractFastDfsOperate implements FastDfsUpload {


    public FastDfsUploadSliceImpl(String trackers, Long slickChunkSize, int port) throws IOException {
        super(trackers, slickChunkSize, port);
    }

    @Override
    public String uploadByBytesAndRetureRemoteId(byte[] bytes, String filename) {
        try {
            String[] uploadResult = storageClient.upload_file(bytes, deduceExtName(filename), null);

            return uploadResult[0] + "/" + uploadResult[1];
        } catch (Exception e) {
            log.info("uploadByBytyAndRetureRemoteId fail：{}", e);
        }
        return null;
    }

    @Override
    public String uploadByInputStreamAndSize(InputStream inputStream, String filename, Long size) throws Exception {

        String fileExtName = deduceExtName(filename);
        return uploadSingle(inputStream, 100000L, fileExtName);
    }


    @Override
    public String uploadFileAndRetureRemoteId(FileInputStream fileInputStream, String filename) {
        try {

            String fileExtName = deduceExtName(filename);

            return uploadFileAndRetureRemoteId(fileInputStream, filename, fileExtName);
        } catch (Exception e) {
            log.info("uploadFileAndRetureRemoteId 上传文件失败:{}", filename, e);
            return null;
        }
    }

    /**
     * FileInputStream 分块上传
     *
     * @param fileInputStream 本地文件流
     * @param filename        本地文件名称
     * @param fileExtName     本地文件扩展名
     * @return
     * @throws Exception
     */
    private String uploadFileAndRetureRemoteId(FileInputStream fileInputStream, String filename, String fileExtName) throws Exception {

        long size = fileInputStream.getChannel().size();

        // 总共需要分为几块
        long totalSplit = size / slickChunkSize + (size % slickChunkSize == 0 ? 0 : 1);


        // 这里是要根据total_split 进行sh

        if (totalSplit == 1) {
            // totalSplit ==1 表示不需要分块
            return uploadSingle(fileInputStream, 100000L, fileExtName);
        }

        // 每块 需要读几次
        long oneSlitFlip = slickChunkSize / READ_CHUNK_SIZE;

        ChunkMeta chunkMeta = new ChunkMeta();

        Map<String, ChunkInfo> chunkInfoMap = new HashMap<>();


        // 整块的数据.注意，最后一次有可能是不是一整块
        for (long i = 0; i < totalSplit; i++) {
            ChunkInfo chunkInfo = new ChunkInfo();

            String chunkLink = uploadSingle(fileInputStream, oneSlitFlip, fileExtName);

            chunkInfo.setChunkLink(chunkLink);
            if (i == totalSplit - 1) {
                chunkInfo.setChunkSize(size - slickChunkSize * (totalSplit - 1));
            } else {
                chunkInfo.setChunkSize(slickChunkSize);
            }
            chunkInfo.setOrderNumber(i);
            chunkInfoMap.put(String.valueOf(i), chunkInfo);
        }

        chunkMeta.setChunkInfoMap(chunkInfoMap);
        chunkMeta.setTotalSize(size);
        chunkMeta.setChunkCount(totalSplit);

        return uploadChunkMetaInfoAndReturnRemoteId(chunkMeta, filename);

    }

    @Override
    public String upload(String localFilename, String fileExtName) {
        try {

            FileInputStream fileInputStream = new FileInputStream(new File(localFilename));

            return uploadFileAndRetureRemoteId(fileInputStream, localFilename, fileExtName);
        } catch (Exception e) {
            log.info("upload 上传文件失败:{}", localFilename, e);
            return null;
        }
    }

    @Override
    public String upload(String localFilename) {
        try {

            String fileExtName = deduceExtName(localFilename);

            FileInputStream fileInputStream = new FileInputStream(new File(localFilename));

            return uploadFileAndRetureRemoteId(fileInputStream, localFilename, fileExtName);
        } catch (Exception e) {
            log.info("upload 上传文件失败:{}", localFilename, e);
            return null;
        }
    }

    /**
     * 上传单个
     *
     * @param flipNum 需要读取的次数
     * @return
     */
    private String uploadSingle(InputStream fileInputStream, Long flipNum, String fileExtName) throws Exception {

        byte[] chunk = new byte[READ_CHUNK_SIZE];
        int read = fileInputStream.read(chunk);

        if (read <= 0) {
            log.info("uploadSingle flipNum:{} 读取不到相关的数据 return null", flipNum);
        }

        // 上面已经读取了一次了
        Long flipCount = 1L;

        String[] result = storageClient.upload_appender_file(null, chunk, 0, read, fileExtName, null);
        log.info("first result: {}", JSON.toJSONString(result));

        int length;
        // 如果是小于翻页的次数，并且没有还有读取到内容，就继续读取
        while ((flipCount.compareTo(flipNum) < 0
                && (length = fileInputStream.read(chunk)) != -1)) {
            storageClient.append_file(result[0], result[1], chunk, 0, length);
            ++flipCount;
        }

        return result[0] + "/" + result[1];
    }


    /**
     * 上传meta信息
     *
     * @param meta
     * @return
     */
    @Override
    public String uploadChunkMetaInfoAndReturnRemoteId(ChunkMeta meta, String filename) {

        String content = CHUNK_FLAG + JSONObject.toJSONString(meta);

        return uploadByBytesAndRetureRemoteId(content.getBytes(), filename);
    }

}


