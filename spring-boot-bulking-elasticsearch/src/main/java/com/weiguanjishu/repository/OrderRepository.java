package com.weiguanjishu.repository;


import com.weiguanjishu.model.OrderModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 微信公众号：微观技术
 */

public interface OrderRepository extends ElasticsearchRepository<OrderModel, Long> {
}
