## Spring_Boot自带定时任务的支持

### 常见的定时任务的区别：

1.Java自带的java.util.Timer类    缺点：配置麻烦，时间出现延后问题

2.Quartz框架  

3.SpringBoot使用注解方式开启定时任务 

* 启动类里面 @EnableScheduling开启定时任务，自动扫描
* 定时任务业务类 加注解 @Component被容器扫描
* 定时执行的方法加上注解 @Scheduled(fixedRate=2000) 定期执行一次

测试定时任务

```java
 @Componentpublic class TestTask {    
     @Scheduled(fixedRate=2000)    
     public void sum() {       
         System.out.println(new Date()+"定时器被执行...");  
     }}
```

![控制台输出](https://i.loli.net/2019/09/06/KEjp5HNo7fVYOWF.png)

SpringBoot常用定时任务表达式配置

1.cron 定时任务表达式 @Scheduled(cron="*/1 * * * * *") 表示每秒
2.fixedRate: 定时多久执行一次（上一次开始执行时间点后xx秒再次执行；）
3.fixedDelay: 上一次执行结束时间点后xx秒再次执行
4.fixedDelayString:  字符串形式，可以通过配置文件指定

### 异步任务

异步任务和使用场景：默认的情况下开启的是同步的任务，执行耗时较长，开启异步只会计算最后一次任务的时间，适用于处理log、发送邮件、短信……等下单接口

步骤：

1.启动类里面使用@EnableAsync注解开启功能，自动扫描

2.定义异步任务类并使用@Component标记组件被容器扫描,异步方法加上@Async

3.通过注入方式，注入到controller里面，如果测试前后区别则改为同步则把Async注释掉



![异步](C:\Users\神秘V\AppData\Roaming\Typora\typora-user-images\1567740585290.png)

![同步](C:\Users\神秘V\AppData\Roaming\Typora\typora-user-images\1567740720240.png)

注意点：

* 要把异步任务封装到类里面，不能直接写到Controller
* 增加Future<String> 返回结果 AsyncResult<String>("task执行完成");  
* 如果需要拿到结果 需要判断全部的 task.isDone()