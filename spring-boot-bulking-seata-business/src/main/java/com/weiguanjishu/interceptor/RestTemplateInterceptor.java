package com.weiguanjishu.interceptor;

import io.seata.core.context.RootContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    public RestTemplateInterceptor() {
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);
        String xid = RootContext.getXID();
        if (StringUtils.isNotEmpty(xid)) {
            requestWrapper.getHeaders().add(RootContext.KEY_XID, xid);
        }

        return clientHttpRequestExecution.execute(requestWrapper, bytes);
    }
}
