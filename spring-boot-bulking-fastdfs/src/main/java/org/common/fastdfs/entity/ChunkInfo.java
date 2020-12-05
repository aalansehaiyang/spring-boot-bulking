package org.common.fastdfs.entity;

import lombok.Data;

/**
 * Created by louxiu
 */

@Data
public class ChunkInfo {
    /**
     * 块在存储服务的地址
     */
    String chunkLink;
    /**
     * 块的序号
     */
    Long orderNumber;
    /**
     * 块大小
     */
    Long chunkSize;
}
