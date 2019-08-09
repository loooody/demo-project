package com.lv.demo.controller;

import com.lv.demo.dao.UserDao;
import com.lv.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/es")
public class UserController {
    @Autowired
    UserDao userDao;

    /**
     * 查询数据库中所有的用户信息
     *
     * @return
     */
    @GetMapping("/getAllUser")
    public ModelAndView getIndex() {
        Map<String, Object> map = new HashMap<>();
        List<User> userList = userDao.findAll();
        map.put("userList", userList);
        return new ModelAndView("list", map);
    }

    /**
     * 向数据库中添加数据
     *
     * @return
     */
    @PostMapping("/save")
    public User saveUser() {
        User user = new User();
        user.setId(10);
        user.setName("xh");
        user.setAge(8);
        user.setCreateTime(new Date());
        return userDao.save(user);
    }
}
