package com.gerrywen.seckill.third.rabbitmq.thread;

import com.gerrywen.seckill.third.rabbitmq.request.Request;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * description:
 * 执行请求的工作线程
 * 线程和队列进行绑定，然后再线程中处理对应的业务逻辑
 *
 * @author wenguoli
 * @date 2020/3/5 8:48
 */
public class RequestThread implements Callable<Boolean> {
    /**
     * 队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    /**
     * 方法中执行具体的业务逻辑
     * TODO
     *
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        return true;
    }
}

