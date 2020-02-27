package com.zb.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class TestTask {
    //    @Scheduled(cron = "0/3 * * * * *")
//    @Scheduled(fixedRate = 3000)
    public void test1() {
        System.out.println("定时打印1~~~~~~~~~");
    }

//        @Scheduled(cron = "0/5 * * * * *")
//    @Scheduled(fixedRate = 5000)
    public void test2() {
        System.out.println("定时打印2~~~~~~~~~");
    }
}
