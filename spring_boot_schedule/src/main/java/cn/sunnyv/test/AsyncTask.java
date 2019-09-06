package cn.sunnyv.test;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 功能描述：异步定时任务
 *
 * @author lihao
 * @create 2019-09-06 10:45
 */
@Component
@Async
public class AsyncTask {

    public void testAsync1() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(1000L);
        long end = System.currentTimeMillis();
        System.out.println("同步任务1耗时:"+(end-begin));
    }

    public void testAsync2() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(2000L);
        long end = System.currentTimeMillis();
        System.out.println("同步任务2耗时:"+(end-begin));
    }

    public void testAsync3() throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(3000L);
        long end = System.currentTimeMillis();
        System.out.println("同步任务3耗时:"+(end-begin));
    }
}
