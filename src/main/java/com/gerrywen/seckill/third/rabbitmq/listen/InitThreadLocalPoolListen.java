package com.gerrywen.seckill.third.rabbitmq.listen;

import com.gerrywen.seckill.third.rabbitmq.thread.RequestThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * description: 系统初始化监听器 初始队列
 *
 * @author wenguoli
 * @date 2020/3/5 8:55
 */
public class InitThreadLocalPoolListen implements ServletContextListener {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 系统初始化队列
     *
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     *
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RequestThreadPool.getInstance();
    }

    /**
     * 监听器销毁执行的逻辑
     *
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     *
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("this is InitThreadLocalPoolListen last destroyed");
    }
}