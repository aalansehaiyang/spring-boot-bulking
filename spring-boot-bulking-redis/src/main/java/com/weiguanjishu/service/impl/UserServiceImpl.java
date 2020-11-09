package com.weiguanjishu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weiguanjishu.domain.mapper.UserMapper;
import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Page<User> querUserByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return userMapper.querUserByPage();
    }
}
