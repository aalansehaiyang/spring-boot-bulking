package com.onlyone;


import com.alibaba.fastjson.JSON;
import com.weiguanjishu.StartApplication;
import com.weiguanjishu.domain.mapper.UserMapper;
import com.weiguanjishu.domain.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 微信公众号：微观技术
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void addUser() {
        User user = User.builder().userName("TomGE").age(29).address("杭州市").build();
        userMapper.addUser(user);
        System.out.println("插入用户成功，uid=" + user.getId());
    }

    @Test
    public void queryAllUser() {
        List<User> users = userMapper.queryAllUser();
        System.out.println(JSON.toJSONString(users));
    }

    @Test
    public void queryUserById() {
        User user = userMapper.queryUserById(1L);
        System.out.println(JSON.toJSONString(user));
    }


}
