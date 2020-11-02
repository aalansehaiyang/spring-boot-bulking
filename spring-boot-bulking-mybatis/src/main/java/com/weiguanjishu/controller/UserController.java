package com.weiguanjishu.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weiguanjishu.common.PageInfo;
import com.weiguanjishu.domain.mapper.UserMapper;
import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    @GetMapping("/add_user")
    public Object add() {
        User user = User.builder().userName("TomGE").age(29).address("上海").build();
        Long uid = userMapper.addUser(user);
        return uid;
    }

    @RequestMapping("/querUserByPage")
    public PageInfo<User> page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        Page<User> users = userService.querUserByPage(pageNo, pageSize);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

}
