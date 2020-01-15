package com.thread;

import com.common.LwzThreadFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @program: muti_thread_test
 * @description: 可重用屏障 测试
 * @author: Linweizhe
 * @create: 2020-01-15 13:25
 **/
public class CyclicBarrierTest {


    public static void main(String[] args) {

        //模拟任务
        List<String> taskList = Arrays.asList("task1", "task2", "task3");

        //创建循环屏障（可重复使用）
        CyclicBarrier cyclicBarrier = new CyclicBarrier(taskList.size());

        //根据自定义线程工厂 创建线程池
        ExecutorService executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new LwzThreadFactory());

        //通过线程池启动线程
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println("开始创建线程" + i);
            executor.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "运行到屏障A了");
                    //等待所有线程执行到此处后继续执行后面的操作
                    cyclicBarrier.await();
                    //模拟任务处理耗时
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "任务执行完毕");

                    //测试屏障的可重用性
                    System.out.println(Thread.currentThread().getName() + "运行到屏障B了");
                    cyclicBarrier.await();
                    //模拟任务处理耗时
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "任务执行完毕");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        //关闭线程池
        executor.shutdown();
    }

}
