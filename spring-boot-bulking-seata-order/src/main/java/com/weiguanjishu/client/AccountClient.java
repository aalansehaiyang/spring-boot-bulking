package com.weiguanjishu.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Component
public class AccountClient {

    @Autowired
    private RestTemplate restTemplate;
    private static String accountURL = "http://127.0.0.1:8081/account/debit?userId=%s&orderMoney=%s";

    public void debit(String userId, BigDecimal orderMoney) {
        String url = String.format(accountURL, userId, orderMoney);
        try {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
            System.out.println("[AccountClient] invoke，result=" + result.getBody());
        } catch (Exception e) {
            log.error("debit url {} ,error:", url, e);
            throw new RuntimeException();
        }
    }
}