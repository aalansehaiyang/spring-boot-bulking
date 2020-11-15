package com.weiguanjishu;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
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

    /**
     * 插入若干记录
     */
    @Test
    public void addUser() {
        for (long i = 1; i < 11; i++) {
            User user = User.builder().id(i).userName("TomGE").age(29).address("杭州").build();
            userMapper.addUser(user);
            System.out.println("插入用户成功，uid=" + user.getId());
        }

    }

    /**
     * 查询所有记录
     */
    @Test
    public void queryAllUser() {
        List<User> users = userMapper.queryAllUser();
        users.sort((t1, t2) -> {
            if (t1.getId() < t2.getId()) {
                return -1;
            } else if (t1.getId() > t2.getId()) {
                return 1;
            } else {
                return 0;
            }
        });

        System.out.println(JSON.toJSONString(users));
    }

    /**
     * 采用 PageHelper 框架分页查询
     */
    @Test
    public void queryAllUserByPage() {
        int pageNo = 3;
        int pageSize = 2;
        PageHelper.startPage(pageNo, pageSize);
        List<User> users = userMapper.queryAllUser();
        users.sort((t1, t2) -> {
            if (t1.getId() < t2.getId()) {
                return -1;
            } else if (t1.getId() < t2.getId()) {
                return 1;
            } else {
                return 0;
            }
        });

        System.out.println(JSON.toJSONString(users));
    }

    /**
     * 单条查询
     */
    @Test
    public void queryUserById() {
        User user = userMapper.queryUserById(9L);
        System.out.println(JSON.toJSONString(user));
    }


}
