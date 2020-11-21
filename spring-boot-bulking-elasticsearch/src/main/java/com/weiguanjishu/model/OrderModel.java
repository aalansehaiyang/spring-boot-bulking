package com.weiguanjishu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author 微信公众号：微观技术
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "order", type = "biz1", shards = 2)
public class OrderModel {

    @Id
    private Long orderId;
    private Double amount;
    private Long buyerUid;
    private String shippingAddress;

}
