package org.common.fastdfs.core;


import org.common.fastdfs.common.MyException;

import java.io.IOException;

/**
 * Created by louxiu
 * <p>
 * 删除远程存储服务文件
 */

public interface FastDfsDelete extends FastDfsOperate {

    boolean delete(String url) throws IOException, MyException;
}
