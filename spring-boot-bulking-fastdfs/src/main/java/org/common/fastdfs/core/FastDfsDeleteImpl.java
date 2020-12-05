package org.common.fastdfs.core;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
//import org.unitedata.scplatform.common.fastdfs.common.MyException;
import org.common.fastdfs.common.MyException;
import org.common.fastdfs.entity.ChunkInfo;
import org.common.fastdfs.entity.ChunkMeta;

import java.io.IOException;
import java.util.Map;

/**
 * Created by louxiu
 * <p>
 * 删除
 */
@Slf4j
public class FastDfsDeleteImpl extends AbstractFastDfsOperate implements FastDfsDelete {

    public FastDfsDeleteImpl(String trackers, int port) throws IOException {
        super(trackers, port);
    }

    @Override
    public boolean delete(String url) throws IOException, MyException {
        String remoteId = null;

        // 如果是链接，那么就要去remoteId
        if (url.startsWith("http")) {
            remoteId = getRempteIdFromUrl(url);
        } else {
            remoteId = url;
        }


        ChunkMeta chunkMeta = getChunkMeta(remoteId);
        if (null != chunkMeta) {
            log.info("delete remoteId:{} has meta:{}", remoteId, chunkMeta);

            for (Map.Entry<String, ChunkInfo> longChunkInfoEntry : chunkMeta.getChunkInfoMap().entrySet()) {
                ChunkInfo value = longChunkInfoEntry.getValue();
                boolean tmpB = deleteFileByRemoteId(value.getChunkLink());
                log.info("delete remoteId: {} chunRemoteId:{} chunkOrderNum:{} result:{}", remoteId, value.getChunkLink(), value.getOrderNumber(), tmpB);
            }

        }

        boolean finalB = deleteFileByRemoteId(remoteId);
        log.info("delete remoteId: {} flag:{} done", remoteId, finalB);


        return true;
    }


    /**
     * 根据remoteId 删除存储服务文件
     *
     * @param remoteId remoteId
     * @return
     * @throws IOException
     * @throws MyException
     */
    private boolean deleteFileByRemoteId(String remoteId) throws IOException, MyException {
        Pair<String, String> groupAndPathFromRemoteId = getGroupAndPathFromRemoteId(remoteId);
        String groupName = groupAndPathFromRemoteId.getKey();
        String fileName = groupAndPathFromRemoteId.getValue();

        this.storageClient.delete_file(groupName, fileName);

        return true;
    }

}
