package com.lv.demo.service.impl;

import com.lv.demo.dao.UserDao;
import com.lv.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<?> getAllUserInfo() {
        return userDao.findAll();
    }
}
