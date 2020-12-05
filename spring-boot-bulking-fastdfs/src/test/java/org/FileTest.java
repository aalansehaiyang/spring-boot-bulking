package org;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.common.fastdfs.ClientGlobal;
import org.common.fastdfs.StorageClient;
import org.common.fastdfs.TrackerClient;
import org.common.fastdfs.TrackerServer;
import org.common.fastdfs.common.MyException;
import org.common.fastdfs.common.NameValuePair;
import org.junit.Test;
import org.springframework.boot.test.json.JsonbTester;

import java.util.Collections;

import java.io.*;
import java.util.List;


/**
 * @author 微信公众号：微观技术
 */

public class FileTest {

    // 上传文件路径。多线程调用，要注意线程安全
    private List<String> fileNames = Collections.synchronizedList(Lists.newArrayList());
    private String groupName = "group1";


    @Test
    public void testUpAndDown() throws InterruptedException, IOException, MyException {
        // 上传文件
        upload();

        // 下载文件
        download();
    }

    /**
     * 上传文件
     */
    private void upload() throws IOException, MyException, InterruptedException {
        ClientGlobal.init("fdfs_client.conf");
        TrackerClient client = new TrackerClient();

        for (int i = 1; i <= 6; i++) {
            String f = "/Users/onlyone/open-github/p/spring-boot-bulking/spring-boot-bulking-fastdfs/source/" + i + ".txt";
            String fileName = i + ".txt";
            Runnable t = () -> {

                TrackerServer trackerServer = null;
                try {
                    trackerServer = client.getConnection();
                } catch (Exception e) {
                }
                // 打印 Socket 信息，验证是否同一个?
                // 结论：每次上传文件，都创建了一个 Socket
                try {
                    System.out.println("Socket=" + trackerServer.getSocket() + "  tracker_group=" + client.getTrackerGroup().tracker_servers.length);
                } catch (Exception e) {
                }

                StorageClient storageClient = new StorageClient(trackerServer, null);

                NameValuePair[] metaList = new NameValuePair[1];
                metaList[0] = new NameValuePair("fileName", fileName);
                File file = new File(f);
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int length = 0;
                try {
                    length = inputStream.available();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[length];
                try {
                    inputStream.read(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String[] result = storageClient.upload_file(bytes, null, metaList);
                    System.out.println("文件名：" + fileName + "，上传地址：" + JSON.toJSONString(result));
                    fileNames.add(result[1]);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            };
            new Thread(t).start();
        }
        Thread.sleep(6000);
    }


    /**
     * 下载文件
     */
    private void download() throws IOException, MyException, InterruptedException {
        ClientGlobal.init("fdfs_client.conf");

        for (String file : fileNames) {

            Runnable t2 = () -> {

                TrackerClient tracker = new TrackerClient();
                TrackerServer trackerServer = null;
                try {
                    trackerServer = tracker.getConnection();
                } catch (IOException e) {
                }
                StorageClient storageClient = new StorageClient(trackerServer, null);

                byte[] result = new byte[0];
                try {
                    result = storageClient.download_file(groupName, file);
                } catch (Exception e) {
                }
//            String local_filename = "1.txt";
//            writeByteToFile(result, local_filename);
//            File file = new File(local_filename);
                System.out.println("文件名：" + file + " ，内容：" + new String(result));
            };

            new Thread(t2).start();
        }

        Thread.sleep(30000);


    }
}
