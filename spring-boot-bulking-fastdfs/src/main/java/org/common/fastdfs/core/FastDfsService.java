package org.common.fastdfs.core;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by louxiu
 */

public interface FastDfsService extends Serializable {

    FastDfsReader getReader(String remoteIdOrUrl, FastDfsSliceReader.ProtocolEnum protocolEnum) throws IOException;

    FastDfsDownload getDownloader() throws IOException;

    FastDfsDelete getDeleter() throws IOException;

    FastDfsUpload getUploader() throws IOException;

    String getTrackerIpAndPort();
}
