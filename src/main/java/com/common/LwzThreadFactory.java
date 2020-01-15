package com.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: muti_thread_test
 * @description: 自定义线程工厂
 * @author: Linweizhe
 * @create: 2020-01-15 13:29
 **/
public class LwzThreadFactory implements ThreadFactory {

    private final String THREAD_NAME = "LWZ";

    private final AtomicInteger THREAD_NUM = new AtomicInteger(1);

    /**
     * 自定义threadFactory 可以实现自定义线程名称等
     * @param r
     * @return
     */
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,THREAD_NAME+THREAD_NUM.getAndIncrement());
    }
}
