package com.weiguanjishu.common;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.Request.Builder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author 微信公众号：微观技术
 */

@Slf4j
public class OkHttpUtil {

    private static OkHttpUtil instance;
    private static OkHttpClient client;

    private static MediaType JSON_TYPE = MediaType.parse("application/json;charset=utf-8");
    private static MediaType FORM_TYPE = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    private static final Integer connTimeout = 10000;
    private static final Integer readTimeout = 10000;
    private static final Integer writeTime = 20000;

    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(connTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(createSslSocketFactory(), x509TrustManager())
                .connectionPool(createConnectionPool())
                .build();
    }

    public static OkHttpUtil getInstance() {

        if (instance == null) {
            instance = new OkHttpUtil();
        }

        return instance;
    }

    /**
     * 设置连接池
     * 1.最大等待连接数
     * 2.连接时间
     */
    private static ConnectionPool createConnectionPool() {
        return new ConnectionPool(400, 5000, TimeUnit.MILLISECONDS);
    }


    /**
     * 信任所有证书
     */
    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

    }

    /**
     * HTTPS证书验证套接字工厂
     */
    private static SSLSocketFactory createSslSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 信任所有链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    /***
     *  Get请求
     */
    public static String httpGet(String url, Map<String, String> headers, Map<String, String> params) {
        Response response = null;
        String res = "";
        log.info("[HttpGet start] URL:{}", url);
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("请求地址不能为空");
        }

        StringBuffer buffer = new StringBuffer(url);
        Builder builder = new Builder();

        try {
            // 添加header
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 请求参数
            if (params != null && !params.isEmpty()) {
                boolean flag = true;
                Iterator iterator = params.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();

                    if (flag) {
                        buffer.append("?" + entry.getKey() + "=" + entry.getValue());
                        flag = false;
                    } else {
                        buffer.append("&" + entry.getKey() + "=" + entry.getValue());
                    }
                }
            }

            log.info("[HttpGet start] URL:{}", buffer);
            Request request = builder.url(buffer.toString()).get().build();
            log.info("[HttpGet finish] URL:{}", request);
            response = client.newCall(request).execute();
            res = response.body().string();
        } catch (IOException var1) {
            log.error("[HttpGet Exception] URL:{}, error:{}", url, var1);
            throw new RuntimeException(var1);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return res;
    }

    /***
     *  POST  form表单请求
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) {
        log.info("[HttpPost start] URL:{}", url);

        FormBody.Builder formBody = new FormBody.Builder();
        String result = null;

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                formBody.add(key, params.get(key));
            }
        }
        RequestBody body = formBody.build();
        Builder request = new Builder();
        request.url(url).addHeader("CONTENT_TYPE", FORM_TYPE.toString()).build();

        // 添加请求头参数
        if (headers != null && !headers.isEmpty()) {
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request r = request.post(body).build();
        Response response = null;
        try {
            response = client.newCall(r).execute();
            ResponseBody responseBody = response.body();

            result = responseBody.string();
            log.info("[HttpPost FORM FINISH] RESULT: {}", result);
        } catch (IOException var2) {
            log.error("[HttpPost FORM Exception] URL:{}, error:{}", url, var2);
            throw new RuntimeException(var2);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return result;
    }

    /***
     *  POST JSON 请求，自定义超时配置
     */
    public static Response postForm(String url, String message, Map<String, String> headers) {
        return httpPost(url, message, headers, 5000, 5000, 5000);
    }


    public static Response httpPost(String url, String message, Map<String, String> headers, Integer connTimeout, Integer readTimeout, Integer writeTime) {
        RequestBody body = RequestBody.create(JSON_TYPE, message);
        Builder builder = new Builder();
        builder.url(url).post(body).addHeader("Accept", "application/json").build();

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
        Request request = builder.build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(connTimeout, TimeUnit.MILLISECONDS).readTimeout(readTimeout, TimeUnit.MILLISECONDS).writeTimeout(writeTime, TimeUnit.MILLISECONDS).build();
        try {
            Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /***
     *  POST JSON 请求
     */
    public static String post(String url, String message, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(JSON_TYPE, message);
        Builder builder = new Builder();
        builder.url(url).post(requestBody).build();
        Response response = null;
        String result = "";
        try {
            // 添加header
            if (headers != null && !headers.isEmpty()) {
                Iterator iterator = headers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    builder.addHeader((String) entry.getKey(), (String) entry.getValue());
                }
            }
            Request request = builder.build();
            response = client.newCall(request).execute();
            if (response.code() == 200) {
                result = response.body() == null ? "" : response.body().string();
            }


        } catch (IOException var3) {
            log.error("[HttpPost JSON Exception] URL:{}, error:{}", url, var3);
            throw new RuntimeException(var3);

        } finally {
            if (response != null) {
                response.close();
            }
        }

        return result;
    }

}
