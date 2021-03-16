package com.weiguanjishu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 微信公众号：微观技术
 */

@Data
@Builder
public class OrderModel {

    private Long orderId;
    private Double amount;
    private Long buyerUid;
    private String shippingAddress;
    private long startTime;

}
