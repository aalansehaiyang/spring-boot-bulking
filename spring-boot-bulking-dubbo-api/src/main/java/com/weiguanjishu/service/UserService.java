package com.weiguanjishu.service;


import com.weiguanjishu.model.User;
import com.weiguanjishu.param.QueryUserParam;

/**
 * @author 微信公众号：微观技术
 */
public interface UserService {

    public User queryUser(QueryUserParam queryUserParam);
}
