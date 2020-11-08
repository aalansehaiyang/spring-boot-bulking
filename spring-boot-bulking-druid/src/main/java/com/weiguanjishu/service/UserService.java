package com.weiguanjishu.service;

import com.github.pagehelper.Page;
import com.weiguanjishu.domain.model.User;

/**
 * @author 微信公众号：微观技术
 */

public interface UserService {

    public Page<User> querUserByPage(int pageNo, int pageSize);
}
