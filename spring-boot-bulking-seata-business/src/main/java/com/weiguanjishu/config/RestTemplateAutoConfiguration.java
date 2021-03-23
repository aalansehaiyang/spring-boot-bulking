package com.weiguanjishu.config;

import com.weiguanjishu.interceptor.RestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Configuration
public class RestTemplateAutoConfiguration {

    @Autowired
    private Collection<RestTemplate> restTemplates;

    @Autowired
    private RestTemplateInterceptor restTemplateInterceptor;


    @PostConstruct
    public void init() {
        if (this.restTemplates != null) {
            Iterator var1 = restTemplates.iterator();

            while (var1.hasNext()) {
                RestTemplate restTemplate = (RestTemplate) var1.next();
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList(restTemplate.getInterceptors());
                interceptors.add(restTemplateInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        }

    }
}
