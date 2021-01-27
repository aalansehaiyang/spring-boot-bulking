package com.weiguanjishu;

import com.alibaba.fastjson.JSON;
import com.weiguanjishu.model.OrderModel;
import com.weiguanjishu.repository.OrderRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class OrderRepositoryTest {

    @Resource
    private OrderRepository orderRepository;

    @Test
    public void test1() {

        OrderModel orderModel = OrderModel.builder()
                .orderId(1L)
                .amount(25.5)
                .buyerUid(13201L)
                .shippingAddress("上海")
                .build();

        orderModel = orderRepository.save(orderModel);
        System.out.println(orderModel);
    }


    @Test
    public void test2() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder);
        List<OrderModel> orderDocumentList = orderRepository.search(searchQueryBuilder.build()).getContent();
        System.out.println(JSON.toJSONString(orderDocumentList));
    }
}
