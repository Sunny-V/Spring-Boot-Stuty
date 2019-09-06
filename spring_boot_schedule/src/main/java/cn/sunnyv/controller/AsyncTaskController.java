package cn.sunnyv.controller;

import cn.sunnyv.domain.JsonData;
import cn.sunnyv.test.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述：
 *
 * @author lihao
 * @create 2019-09-06 11:09
 */
@RestController
@RequestMapping("/api/testAsync")
public class AsyncTaskController {

    @Autowired
    private AsyncTask task;

    @GetMapping("async_task")
    public JsonData testTask() throws InterruptedException {

        long begin = System.currentTimeMillis();
        task.testAsync1();
        task.testAsync2();
        task.testAsync3();
        long end = System.currentTimeMillis();
        long total = end-begin;
        System.out.println("总耗时:"+total);
        return JsonData.buildSuccess(total);
    }
}
