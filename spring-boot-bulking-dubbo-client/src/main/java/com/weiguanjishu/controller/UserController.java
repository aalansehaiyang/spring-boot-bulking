package com.weiguanjishu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.weiguanjishu.model.User;
import com.weiguanjishu.param.QueryUserParam;
import com.weiguanjishu.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Reference
    UserService userService;

    /**
     * http://localhost:8098/query
     */
    @RequestMapping("/query")
    public User query() {
        QueryUserParam param = new QueryUserParam();
        param.setName("微观技术");
        User user = userService.queryUser(param);
        System.out.println(JSON.toJSONString(user));
        return user;
    }

}
