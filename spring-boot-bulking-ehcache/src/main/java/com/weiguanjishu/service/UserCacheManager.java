package com.weiguanjishu.service;

import com.weiguanjishu.domain.model.User;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 微信公众号：微观技术
 */
@Component
public class UserCacheManager {

    @Resource
    private CacheManager cacheManager;

    public User getUserById(Long id) {
        Cache cache = cacheManager.getCache("userCache");
        User user = cache.get(id, User.class);
        if (user == null) {
            System.out.println("缓存中无值");
            user = User.builder().id(id).userName("雪糕(" + id + ")").age(18).address("杭州").build();
            cache.put(id, user);
        }
        return user;
    }

    public User updateUser(User user) {
        user.setUserName("雪糕（new name）");
        Cache cache = cacheManager.getCache("userCache");
        cache.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        Cache cache = cacheManager.getCache("userCache");
        cache.evict(id);
        System.out.println("db 删除数据，id=" + id);
    }
}
