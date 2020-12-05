package org.common.fastdfs.core;

import org.common.fastdfs.entity.ChunkMeta;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by louxiu
 * Fastdfs 上传接口
 */

public interface FastDfsUpload extends FastDfsOperate {

    /**
     * 上传字节， 并返回remoteId(不带地址)
     *
     * @param bytes
     * @param filename
     * @return
     */
    String uploadByBytesAndRetureRemoteId(byte[] bytes, String filename);


    /**
     * 分片上传的meta信息 这里要手动设置下size
     *
     * @param inputStream
     * @param filename
     * @return
     */
    String uploadByInputStreamAndSize(InputStream inputStream, String filename, Long size) throws Exception;


    /**
     * 分片上传。 本地文件输入流
     *
     * @param inputStream
     * @param filename
     * @return
     */
    String uploadFileAndRetureRemoteId(FileInputStream inputStream, String filename);

    /**
     * 上传本地文件，并追加额外后缀名
     *
     * @param fileName
     * @param fileExtName
     * @return
     */
    String upload(String fileName, String fileExtName);

    /**
     * 上传本地文件，并追加额外后缀名
     *
     * @param fileName
     * @return
     */
    String upload(String fileName);


    /**
     * 上传chunkMeta信息
     *
     * @param meta
     * @param filename
     * @return
     */
    public String uploadChunkMetaInfoAndReturnRemoteId(ChunkMeta meta, String filename);

}
