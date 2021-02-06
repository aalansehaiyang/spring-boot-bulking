package com.weiguanjishu.controller;

import com.weiguanjishu.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {


    /**
     * http://localhost:8090/queryUser
     * <p>
     * 插入记录
     */
    @RequestMapping(value = "/queryUser", method = RequestMethod.POST)
    public String queryUser(@RequestBody User user) {
        String result = "已经接收到请求，用户名：%s , 年龄：%s , 地址：%s。响应：sucess!";
        return String.format(result, user.getUserName(), user.getAge(), user.getAddress());
    }

}
