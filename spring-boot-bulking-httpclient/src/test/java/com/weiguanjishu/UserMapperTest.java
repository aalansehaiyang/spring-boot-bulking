package com.weiguanjishu;


import com.alibaba.fastjson.JSON;
import com.weiguanjishu.common.HttpClientUtil;
import com.weiguanjishu.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class UserMapperTest {


    @Test
    public void queryUser() {
        User user = User.builder().userName("TomGE").age(29).address("北京").build();

        String url = "http://localhost:8090/queryUser";
        String requestJson = JSON.toJSONString(user);
//        System.out.println(requestJson);
        String result = HttpClientUtil.postJsonRequest(url, requestJson, null);
        System.out.println("响应结果：");
        System.out.println(result);
    }


}
