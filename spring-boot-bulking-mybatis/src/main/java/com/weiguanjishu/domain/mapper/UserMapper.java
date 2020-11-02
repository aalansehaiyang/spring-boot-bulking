package com.weiguanjishu.domain.mapper;

import com.github.pagehelper.Page;
import com.weiguanjishu.domain.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 微信公众号：微观技术
 */

@Mapper
public interface UserMapper {

    Long addUser(User user);

    List<User> queryAllUser();

    User queryUserById(Long id);

    Page<User> querUserByPage();
}