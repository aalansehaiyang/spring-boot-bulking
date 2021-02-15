package com.weiguanjishu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.weiguanjishu.model.User;
import com.weiguanjishu.param.QueryUserParam;
import org.springframework.stereotype.Component;


@Component
@Service
public class DubboServiceImpl implements UserService {

    @Override
    public User queryUser(QueryUserParam queryUserParam) {

        System.out.println("请求参数，queryUserParam：" + queryUserParam.getName());

        User user = User.builder().id(1L).userName("微观技术").age(11).address("上海").build();
        return user;
    }
}
