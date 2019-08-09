package com.lv.demo.dao;

import com.lv.demo.entity.EsUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserEsDaoTest {

    private static final Logger log = LoggerFactory.getLogger(UserEsDaoTest.class);

    @Autowired
    private UserEsDao userEsDao;

    @Test
    public void getUserList() {
//        Optional<EsUser> userList = userEsDao.findById(1);
//        log.info("获取到的数据为: {}" + userList);
//        EsUser es = new EsUser();
//        es.setId(7);
//        es.setName("lalalal");
//        es.setAge(66);
//        es.setCreate_time(new Date());
//        userEsDao.save(es);
//        log.info("success");
    }

    @Test
    public void getAllUser() {
        Iterable<EsUser> userList =  userEsDao.findAll();
        userList.forEach(System.out::print);
        log.info("所有的用户信息为: {}" + userList);
    }

}