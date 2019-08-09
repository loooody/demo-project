package com.lv.demo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class QuartzTest {

    private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(cron = "${jobs.corn}")
//    public void getTask() {
//        System.out.println("当前时间：" + df.format(new Date()));
//    }

}
