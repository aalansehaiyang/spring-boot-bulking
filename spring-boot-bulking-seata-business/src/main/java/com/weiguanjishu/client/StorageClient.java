package com.weiguanjishu.client;

import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class StorageClient {

    @Autowired
    private RestTemplate restTemplate;

    private static String storageURL = "http://127.0.0.1:8083/api/storage/deduct?commodityCode=%s&count=%s";

    public void deduct(String commodityCode, int orderCount) {
        System.out.println("invoke storage， xid: " + RootContext.getXID());

        String url = String.format(storageURL, commodityCode, orderCount);
        try {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
            System.out.println("[StorageClient] invoke，result=" + result.getBody());
        } catch (Exception e) {
            log.error("deduct url {} ,error:", url, e);
            throw new RuntimeException();
        }
    }
}
