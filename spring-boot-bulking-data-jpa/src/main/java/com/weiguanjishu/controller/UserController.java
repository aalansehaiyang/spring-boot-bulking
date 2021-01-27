package com.weiguanjishu.controller;

import com.weiguanjishu.entity.User;
import com.weiguanjishu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author 微信公众号：微观技术
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * http://localhost:8090/add_user
     * <p>
     * 插入记录
     */
    @RequestMapping("/add_user")
    public Object add() {
        User user = User.builder().userName("TomGE").age(29).address("上海").build();
        User result = userRepository.save(user);
        return "用户id: " + result.getId();
    }

    /**
     * http://localhost:8090/add_user_with_id
     */
    @RequestMapping("/add_user_with_id")
    public Object addUserWithId() {
        Long id = Long.valueOf(new Random().nextInt(100) + 100);
        User user = User.builder().id(id).userName("TomGE").age(22).address("杭州").build();
        User result = userRepository.save(user);
        return "用户id: " + result.getId();
    }

    /**
     * 根据id 查询
     */
    @RequestMapping("/queryById")
    public User queryById() {
        User result = userRepository.getOne(2L);
        return result;
    }

    /**
     * 根据userName 查询
     */
    @RequestMapping("/findByUserName")
    public List<User> findByUserName() {
        String userName = "TomGE";
        List<User> result = userRepository.findByUserName(userName);
        return result;
    }

    /**
     * 根据userName 和 address 组合查询
     */
    @RequestMapping("/queryByUserNameAndAddress")
    public List<User> queryByUserNameAndAddress() {
        String userName = "TomGE";
        String address = "上海";
        List<User> result = userRepository.queryByUserNameAndAddress(userName, address);
        return result;
    }

    /**
     * 根据userName 和 address 组合查询
     */
    @RequestMapping("/findByUserNameAndAddress")
    public List<User> findByUserNameAndAddress() {
        String userName = "TomGE";
        String address = "上海";
        List<User> result = userRepository.findByUserNameAndAddress(userName, address);
        return result;
    }

    /**
     * 测试排序
     */
    @RequestMapping("/sortById")
    public List<User> sortById() {

        List<User> result = userRepository.findAll(new Sort(Sort.Direction.DESC, "id"));

        return result;

    }

    /**
     * http://localhost:8090/page?pageNo=1&pageSize=3
     * <p>
     * 测试分页
     */
    @RequestMapping("/page")
    public Page<User> page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        Page<User> pageUser = userRepository.findAll(new PageRequest(pageNo, pageSize));

        return pageUser;
    }

    /**
     * http://localhost:8090/conditionWithPage?pageNo=0&pageSize=3
     * <p>
     * 测试分页
     */
    @RequestMapping("/conditionWithPage")
    public List<User> conditionWithPage(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        String address = "上海";
        List<User> pageUser = userRepository.findByAddress(address, new PageRequest(pageNo, pageSize));
        return pageUser;
    }


}
