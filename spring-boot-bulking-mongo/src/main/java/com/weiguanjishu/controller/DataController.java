package com.weiguanjishu.controller;

import com.weiguanjishu.entity.Address;
import com.weiguanjishu.entity.User;
import com.weiguanjishu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author 微信公众号：微观技术
 */

@RestController
public class DataController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/save")
    public User save() {
        User p = User.builder().id(112L).name("TomGE").age(26).build();
        Collection<Address> addresses = new LinkedHashSet<Address>();
        Address address1 = new Address(1L, "上海");
        Address address2 = new Address(2L, "杭州");
        Address address3 = new Address(3L, "广州");
        Address address4 = new Address(4L, "苏州");
        addresses.add(address1);
        addresses.add(address2);
        addresses.add(address3);
        addresses.add(address4);
        p.setAddresses(addresses);

        return userRepository.save(p);
    }

    @RequestMapping("/queryByName")
    public User queryByName(String name) {
        return userRepository.findByName(name);
    }

    @RequestMapping("/queryByAge")
    public List<User> queryByAge(Integer age) {
        return userRepository.withQueryFindByAge(age);
    }

}