package com.weiguanjishu.repository;

import com.weiguanjishu.model.ProductModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 微信公众号：微观技术
 */

public interface ProductRepository extends ElasticsearchRepository<ProductModel, Long> {

}
