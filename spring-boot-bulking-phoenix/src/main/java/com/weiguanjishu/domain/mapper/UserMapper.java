package com.weiguanjishu.domain.mapper;

import com.weiguanjishu.domain.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 微信公众号：微观技术
 */

@Mapper
public interface UserMapper {

    @Insert("upsert into user" +
            " (id,user_name,age,address)" +
            " values" +
            " (#{id}, #{userName}, #{age}, #{address})")
    Long addUser(User user);

    @Select("select * from user")
    List<User> getAll();

}