package com.weiguanjishu.repository;

import com.github.pagehelper.Page;
import com.weiguanjishu.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 微信公众号：微观技术
 */
public interface UserRepository extends JpaRepository<User, Long> {


    List<User> findByUserName(String userName);

    List<User> findByUserNameAndAddress(String userName, String address);

    // 使用 @Query查询，参数按照名称绑定
    @Query("select u from User u where u.userName= :userName and u.address= :address")
    List<User> queryByUserNameAndAddress(@Param("userName") String userName, @Param("address") String address);

    // 查询条件+分页
    List<User> findByAddress(String address, Pageable pageable);
}
