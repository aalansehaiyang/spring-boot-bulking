package org.common.fastdfs.entity;

import lombok.Data;

import java.util.Map;

/**
 * Created by louxiu
 */

@Data
public class ChunkMeta {
    /**
     * 文件总大小(byte)
     */
    Long totalSize;
    /**
     * 分块的数目
     */
    Long chunkCount;
    /**
     * 分块信息
     */
    Map<String, ChunkInfo> chunkInfoMap;
}