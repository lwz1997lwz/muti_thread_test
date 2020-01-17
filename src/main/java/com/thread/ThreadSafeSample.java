package com.thread;

import com.common.LwzThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: muti_thread_test
 * @description:
 * @author: Linweizhe
 * @create: 2020-01-17 10:29
 **/
public class ThreadSafeSample {

    private int sharedState;

    /**
     * 使用synchronized加锁，将两次赋值操作使用synchronized保护起来，this作为互斥单元，避免其他线程并发的修改sharedState
     */
    private void safeAction() {
        while (sharedState < 100000) {
            synchronized (this) {
                int former = sharedState++;
                int latter = sharedState;
                if (former != latter - 1) {
                    System.out.println("Observed data race, former is " +
                            former + ", " + "latter is " + latter);
                }
            }
        }
    }


    private void nonSafeAction() {
        while (sharedState < 100000) {
            int former = sharedState++;
            int latter = sharedState;
            if (former != latter - 1) {
                System.out.println("Observed data race, former is " +
                        former + ", " + "latter is " + latter);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeSample sample = new ThreadSafeSample();
        ExecutorService executorService = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new LwzThreadFactory());

        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            sample.nonSafeAction();
        });

        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            sample.nonSafeAction();
        });

        //关闭线程池
        executorService.shutdown();
    }
}