package com.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: muti_thread_test
 * @description: 多线程倒计时器测试
 * @author: Linweizhe
 * @create: 2020-01-15 10:02
 **/
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        //模拟任务
        List<String> taskList = Arrays.asList("task1", "task2", "task3");

        //创建倒计时器
        CountDownLatch countDownLatch = new CountDownLatch(taskList.size());

        //自定义threadFactory 可以实现自定义线程名称等
        ThreadFactory lwzThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadNum = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                String threadName = "lwz";
                return new Thread(r, threadName + threadNum.getAndIncrement());
            }
        };

        //创建线程池
        ExecutorService executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),lwzThreadFactory);

        //使用线程池开启线程
        taskList.forEach(e -> {
            //使用了匿名表达式 简化了 匿名内部类（实现runnable接口）
            executor.submit(() -> {
                try {
                    //模拟执行任务耗时操作
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "开始执行任务:" + e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            });
        });

        //关闭线程池
        executor.shutdown();
        //阻塞，等待新开启的线程全部执行完
        countDownLatch.await();

        System.out.println("主程序");
    }
}
