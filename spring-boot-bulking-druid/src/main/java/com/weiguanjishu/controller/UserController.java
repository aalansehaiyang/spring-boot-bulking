package com.weiguanjishu.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weiguanjishu.common.PageInfo;
import com.weiguanjishu.domain.mapper.UserMapper;
import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    /**
     * 新增用户
     * http://localhost:8090/add_user
     */
    @GetMapping("/add_user")
    public Object add() {
        User user = User.builder().userName("TomGE").age(29).address("上海").build();
        Long successCount = userMapper.addUser(user);
        return "用户id: " + user.getId();
    }

    /**
     * 分页查询用户
     * http://localhost:8090/querUserByPage?pageNo=2&pageSize=4
     */
    @RequestMapping("/querUserByPage")
    public PageInfo<User> page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        Page<User> users = userService.querUserByPage(pageNo, pageSize);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    /**
     * 查询所有用户
     */
    @RequestMapping("/queryAllUser")
    public List<User> queryAllUser() {
        List<User> users = userMapper.queryAllUser();
        return users;
    }

}
