package org.common.fastdfs.core;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by louxiu
 */
@Slf4j
public class FastDfsDownloadImpl extends AbstractFastDfsOperate implements FastDfsDownload {

    public FastDfsDownloadImpl(String trackers, int port) throws IOException {
        super(trackers, port);
    }

    @Override
    public byte[] downloadByRemoteId(String remoteId) throws IOException {
        return ByteStreams.toByteArray(getInputStreamByRemoteId(remoteId));
    }

    @Override
    public void downloadToLocalTempPath(String tempPath, String urlOrRemoteId) throws Exception {

        long start = System.currentTimeMillis();
        Path path = Paths.get(tempPath);
        InputStream in = null;
        FileOutputStream output = null;
        if (!Files.exists(path.getParent())) {
            try {
                // 如果父目录不存在就创建
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
            output = new FileOutputStream(tempPath);
            // 这历史考虑整体的文件
            in = getInputStream(getRempteIdFromUrl(urlOrRemoteId));

            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            long end = System.currentTimeMillis();
            log.info("[FastDfsDownloadImpl] download to local temp path: {}, url: {}, rt: {}",
                    tempPath, urlOrRemoteId, (end - start));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    @Override
    public InputStream getInputStreamByRemoteId(String remoteId) throws IOException {
        return getInputStream(remoteId);
    }
}
