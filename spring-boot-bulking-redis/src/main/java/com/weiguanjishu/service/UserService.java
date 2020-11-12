package com.weiguanjishu.service;

import com.weiguanjishu.domain.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * @author 微信公众号：微观技术
 */

@Component
@CacheConfig(cacheNames = "user")
public class UserService {

    @Cacheable(key = "#id")
    public User getUserById(Long id) {
        System.out.println("缓存中无值");
        User user = User.builder().id(id).userName("雪糕( " + id + ")").age(18).address("杭州").build();
        return user;
    }

    @CachePut(key = "#user.id")
    public User updateUser(User user) {
        user.setUserName("雪糕（新名称）");
        return user;
    }

    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        System.out.println("db删除数据：" + id);
    }
}
