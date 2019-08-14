package com.lv.demo.controller;

import com.lv.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/getUserList")
    public List<?> getAllUserList() {
        List<?> userList = userService.getAllUserInfo();
        if (userList == null) {
            log.info("【查询所有用户信息】:用户信息为空");
        }
        return userList;
    }

}
