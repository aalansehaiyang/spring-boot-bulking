package com.weiguanjishu.common;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 微信公众号：微观技术
 */

@Slf4j
public class HttpClientUtil {

    // 默认连接超时时间（毫秒）
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    // 默认读超时时间（毫秒）
    private static final int DEFAULT_READ_TIMEOUT = 30000;
    // 连接池获取线程超时时间（毫秒）
    private static final int CONNECTION_REQUEST_TIMEOUT = 10000;
    // 线程池最大数量
    private static final int MAX_TOTAL = 200;
    // 每次并行发送请求数量
    private static final int MAX_PRE_ROUTE = 50;
    // 可用空闲连接过期时间（毫秒）,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立。
    private static final int VALIDATE_AFTER_INACTIVITY = 30000;

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static PoolingHttpClientConnectionManager cm;

    private static RequestConfig requestConfig;

    private static CloseableHttpClient httpClient = null;

    static {

        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            log.error("创建SSL连接失败");
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(MAX_TOTAL);
        cm.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);
        cm.setDefaultMaxPerRoute(MAX_PRE_ROUTE);

        requestConfig = RequestConfig.custom()
                .setSocketTimeout(DEFAULT_READ_TIMEOUT)//数据传输过程中数据包之间间隔的最大时间
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)//连接建立时间，三次握手完成时间
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setExpectContinueEnabled(false)//重点参数
                .build();
    }

    public static CloseableHttpClient getHttpClient() {
        if (null == httpClient) {
            httpClient = initHttpClient();
        }
        return httpClient;
    }

    public synchronized static CloseableHttpClient initHttpClient() {
        if (null != httpClient) {
            return httpClient;
        }
        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                Args.notNull(response, "HTTP response");
                final HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    final HeaderElement he = it.nextElement();
                    final String param = he.getName();
                    final String value = he.getValue();
                    if (value != null && "timeout".equalsIgnoreCase(param)) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (final NumberFormatException ignore) {
                        }
                    }
                }
                return 1;
            }

        };
        httpClient = HttpClients.custom().setConnectionManagerShared(true)
                .setConnectionManager(cm)
                .setKeepAliveStrategy(myStrategy)
                .evictExpiredConnections()
                .build();
        return httpClient;
    }

    public static String postRequest(String url, Map<String, String> parameterMap) {
        try {
            return postRequest(url, parameterMap, new HashMap<>());
        } catch (Exception e) {
            log.error("request [{}] post error, e:{}", url, e.getMessage());
            return null;
        }
    }

    public static String postRequest(String url, Map<String, String> parameterMap, Map<String, String> header) {
        return postRequest(url, parameterMap, header, true);
    }

    public static String postRequest(String url, Map<String, String> parameterMap, Map<String, String> header, boolean logData) {
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if (logData) {
                log.debug("postRequest url is {},post data is {}", url, parameterMap);
            }
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = getHttpClient().execute(httpPost,
                    HttpClientContext.create());
            String result = getContent(response);
            return result;
        } catch (Exception e) {
            log.error("post error, e:{}, url:{},parameterMap:{}", e.getMessage(), url,
                    JSON.toJSONString(parameterMap));
            return null;
        } finally {
            release(httpPost, response);
        }
    }

    public static String getWithRetry(String url, HttpHost proxy) {
        int retryCount = 0;
        int maxRetryCount = 3;

        String result = get(url, proxy);
        while (null == result && retryCount < maxRetryCount) {
            retryCount++;
            log.error("retry count:{},url:{}", retryCount, url);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            result = get(url, proxy);
        }
        return result;
    }

    public static String get(String url, HttpHost proxy) {
        HttpGet httpGet = null;
        HttpResponse response = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            if (null == proxy) {
                response = getHttpClient().execute(httpGet);
            } else {
                response = getHttpClient().execute(proxy, httpGet);
            }
            String result = getContent(response);
            return result;
        } catch (Exception e) {
            log.error("get exception:{},url:{}", e.getMessage(), url);
            return null;
        } finally {
            release(httpGet, response);
        }
    }

    public static void release(HttpUriRequest request, HttpResponse response) {
        if ((null == response || response.getStatusLine() == null ||
                response.getStatusLine().getStatusCode() >= 300 ||
                response.getStatusLine().getStatusCode() < 200) && null != request) {
            request.abort();
        }
    }

    /**
     * 获得响应HTTP实体内容
     *
     * @param response
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private static String getContent(HttpResponse response) throws IOException, UnsupportedEncodingException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity, DEFAULT_CHARSET);
        }
        return null;
    }

    public static String getWithNoLog(String url, int connectTimeout, HttpHost proxy) {
        return get(url, proxy);
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param postData
     * @return
     */
    public static String postJsonRequest(String url, String postData, HttpHost proxy) {
        log.error("接受请求url：{}", url);
        Map<String, String> contentTypeHeader = new HashMap<String, String>();
        contentTypeHeader.put("Content-Type", "application/json; charset=UTF-8");
        return postJsonRequest(url, postData, contentTypeHeader, proxy);
    }

    public static String postJsonRequestWithRetry(String url, String postData, HttpHost proxy) {
        Map<String, String> contentTypeHeader = new HashMap<String, String>();
        contentTypeHeader.put("Content-Type", "application/json; charset=UTF-8");
        return postJsonRequestWithRetry(url, postData, contentTypeHeader, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, proxy);
    }

    public static String postJsonRequestWithRetry(String url, String postData, Map<String, String> header, HttpHost proxy) {
        return postJsonRequestWithRetry(url, postData, header, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, proxy);
    }

    public static String postJsonRequest(String url, String postData, Map<String, String> mapHeader, HttpHost proxy) {
        return postJsonRequest(url, postData, mapHeader, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, proxy);
    }

    public static String postJsonRequest(String url, String postData, Map<String, String> mapHeader, int connTimeout, int readTimeout, HttpHost proxy) {
        if (postData == null) {
            return null;
        }

        return doPost(url, postData, mapHeader, proxy);
    }

    public static String postJsonRequestWithRetry(String url, String postData, Map<String, String> mapHeader, int connTimeout, int readTimeout, HttpHost proxy) {
        if (postData == null) {
            return null;
        }

        int retryCount = 0;
        String result = doPost(url, postData, mapHeader, proxy);
        while (null == result && retryCount < 3) {
            retryCount++;
            log.error("postJsonRequestWithRetry retryCount:{},url:{},postData:{}", retryCount, url, postData);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            result = doPost(url, postData, mapHeader, proxy);
        }
        return result;
    }

    private static String doPost(String url, String postData, Map<String, String> mapHeader, HttpHost proxy) {
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            for (Map.Entry<String, String> entry : mapHeader.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(new StringEntity(postData, DEFAULT_CHARSET));
            if (proxy == null) {
                response = getHttpClient().execute(httpPost,
                        HttpClientContext.create());
            } else {
                response = getHttpClient().execute(proxy, httpPost,
                        HttpClientContext.create());
            }

            String result = getContent(response);
            return result;
        } catch (Exception e) {
            log.error("post error, e:{}, url:{},parameterMap:{}", e.getMessage(), url,
                    postData, e);
            return null;
        } finally {
            release(httpPost, response);
        }
    }
}