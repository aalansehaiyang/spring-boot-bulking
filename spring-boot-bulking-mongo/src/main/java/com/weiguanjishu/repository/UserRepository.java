package com.weiguanjishu.repository;

import com.weiguanjishu.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author 微信公众号：微观技术
 */

public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);

    /**
     * 小于：$lt
     * 小于或等于：$lte
     * 大于：$gt
     * 大于或等于：$gte
     * 不等于：$ne
     * 属于：$in
     */
    @Query("{'age': { '$lt' : ?0}}")
    List<User> withQueryFindByAge(Integer age);

}
