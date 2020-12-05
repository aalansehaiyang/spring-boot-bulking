package org.common.fastdfs.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by louxiu
 */
@Slf4j
public class FastDfsSliceReader extends AbstractFastDfsOperate implements FastDfsReader {

    /**
     * 文件协议
     */
    public enum ProtocolEnum {
        Http;
    }

    /**
     * fastdfs 地址 或者是本地文件路径
     */
    private String url;
    private final String COMMA = ",";
    private ProtocolEnum protocolEnum = ProtocolEnum.Http;


    public FastDfsSliceReader(String trackers, String path, ProtocolEnum protocolEnum, int port) throws IOException {
        super(trackers, port);
        this.url = path;
        this.protocolEnum = protocolEnum;
    }


    @Override
    public List<Map<String, Object>> readAll() {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            reader = setReader();
            // 忽略表头
            String line = reader.readLine();
            String[] headers = line.split(COMMA, -1);

            List<Map<String, Object>> list = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(COMMA, -1);
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    map.put(headers[i], split[i]);
                }
                list.add(map);
            }

            return list;
        } catch (Exception e) {
            log.error("读取CSV文件异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private BufferedReader reader = null;
    private String[] headers = null;

    public Map<String, Object> readLine() {
        try {
            reader = setReader();

            String line = reader.readLine();
            if (line == null) {
                return null;
            }

            String[] split = line.split(COMMA, -1);
            if (headers == null) {
                headers = split;
                line = reader.readLine();
                if (line == null) {
                    return null;
                }
                split = line.split(COMMA, -1);
            }

            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                map.put(headers[i], split[i]);
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> readBatch(int batchSize) {
        List<Map<String, Object>> batchRecords = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            Map<String, Object> map = readLine();
            if (map == null) break;
            batchRecords.add(map);
        }
        return batchRecords;
    }

    public String readLineStr() {
        try {
            reader = setReader();

            String line = reader.readLine();
            if (line == null) {
                return null;
            }

            if (headers == null) {
                String[] split = line.split(COMMA, -1);
                headers = split;
                line = reader.readLine();
            }

            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, List<Object>> readLinesByNum(int num) {
        try {
            reader = setReader();

            // 判断headers是否为null
            if (headers == null) {
                String line = reader.readLine();
                if (null == line) {
                    log.info("没有读取到header");
                    return null;
                }
                headers = line.split(COMMA, -1);
            }

            List<String> lineList = new ArrayList<>();

            for (int i = 0; i < num; i++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lineList.add(line);
            }

            // 如果lineList 为空
            if (lineList.size() == 0) {
                log.info("读取到行数为0");
                return null;
            }

            // 给map初始化
            Map<String, List<Object>> map = new HashMap<>();

            for (String header : headers) {
                map.put(header, new ArrayList<>());
            }


            for (String line : lineList) {
                String[] split = line.split(COMMA, -1);
                for (int i = 0; i < headers.length; i++) {
                    map.get(headers[i]).add(StringUtils.isEmpty(split[i]) ? null : split[i]);
                }
            }

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public

    @Override
    public String getContentByRemoteId() throws MalformedURLException {
        return getContentByRemoteId(getRempteIdFromUrl(this.url));
    }

    public String[] getHeaders() {
        try {
            reader = setReader();
            if (null == headers) {
                String[] split = reader.readLine().split(COMMA);
                headers = split;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return headers;
    }

    private BufferedReader setReader() throws IOException {
        if (null != reader) {
            return reader;
        }

        switch (protocolEnum) {
            case Http:
                String remoteId = getRempteIdFromUrl(url);
                return new BufferedReader(new InputStreamReader(getInputStream(remoteId)));

        }
        return null;
    }


}
