package com.gerrywen.seckill.third.rabbitmq.listen;

import com.gerrywen.seckill.third.rabbitmq.thread.RequestThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * description: 系统初始化监听器 初始队列
 *
 * @author wenguoli
 * @date 2020/3/5 8:55
 */
public class InitThreadLocalPoolListen implements ServletContextListener {
    /**
     * 系统初始化队列
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RequestThreadPool.getInstance();
    }

    /**
     * 监听器销毁执行的逻辑
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}