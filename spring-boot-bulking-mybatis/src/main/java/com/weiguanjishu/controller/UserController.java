package com.weiguanjishu.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weiguanjishu.common.PageInfo;
import com.weiguanjishu.domain.mapper.UserMapper;
import com.weiguanjishu.domain.model.User;
import com.weiguanjishu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    /**
     * http://localhost:8090/add_user
     */
    @GetMapping("/add_user")
    public Object add() {
        User user = User.builder().userName("TomGE").age(29).address("上海").build();
        Long successCount = userMapper.addUser(user);
        return "用户id: " + user.getId();
    }

    /**
     * 分页查询用户
     * http://localhost:8090/querUserByPage?pageNo=2&pageSize=4
     */
    @RequestMapping("/querUserByPage")
    public PageInfo<User> page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        Page<User> users = userService.querUserByPage(pageNo, pageSize);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @GetMapping("/insert_batch")
    public Object insertBatch(@RequestParam("batch") int batch) {

        // 设置批次 batch = 100000，共插入 1000W 条数据
        for (int j = 1; j <= batch; j++) {
            List<User> userList = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                int age = i % 2 == 0 ? 28 : 29;
                User user = User.builder().userName("Tom哥-" + ((j - 1) * 100 + i)).age(age).address("上海").build();
                userList.add(user);
            }
            userMapper.insertBatch(userList);
        }
        return "success";
    }

    @GetMapping("/delete_batch")
    public Object deleteBatch(@RequestParam("batch") int batch) {

        // 设置批次 batch = 100000
        for (int j = 1; j <= batch; j++) {
            List<Long> idList = new ArrayList<>();
            for (int i = 1; i <= 100; i += 2) {
                idList.add((long) ((j - 1) * 100 + i));
            }
            userMapper.deleteUser(idList);
        }
        return "success";
    }


}
