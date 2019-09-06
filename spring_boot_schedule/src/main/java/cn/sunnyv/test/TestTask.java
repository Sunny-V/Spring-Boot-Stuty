package cn.sunnyv.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 功能描述：定时任务
 *
 * @author lihao
 * @create 2019-09-06 9:02
 */
@Component
public class TestTask {


    @Scheduled(fixedRate=2000)
    public void sum1() {
        System.out.println(new Date()+"定时器被执行...");
    }

    @Scheduled(cron="*/9 * * * * *")
    public void sum2() {
        System.out.println(new Date()+"定时器被执行...");
    }


}
