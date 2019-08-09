package com.lv.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class QuartzTask {
    Logger log = LoggerFactory.getLogger(QuartzTask.class);

    public void run() {
        log.info("定时任务执行了。。。" + new Date());
        System.out.println("定时任务执行了。。。" + new Date());
    }

}
