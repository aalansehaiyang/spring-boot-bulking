package com.weiguanjishu.controller;

import com.weiguanjishu.domain.mapper.UserMapper;
import com.weiguanjishu.domain.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;


    /**
     * http://localhost:8090/add_user
     */
    @GetMapping("/add_user")
    public Object add() {
        Long id = Long.valueOf(new Random().nextInt(1000));
        User user = User.builder().id(id).userName("TomGE").age(29).address("上海").build();
        Long successCount = userMapper.addUser(user);
        return "用户id: " + user.getId();
    }


    @GetMapping("/getAll")
    public Object getAll() {
        List<User> list = userMapper.getAll();
        return "用户id: ";
    }


//    /**
//     * 分页查询用户
//     * http://localhost:8090/querUserByPage?pageNo=2&pageSize=4
//     */
//    @RequestMapping("/querUserByPage")
//    public PageInfo<User> page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
//        Page<User> users = userService.querUserByPage(pageNo, pageSize);
//        PageInfo<User> pageInfo = new PageInfo<>(users);
//        return pageInfo;
//    }

}
