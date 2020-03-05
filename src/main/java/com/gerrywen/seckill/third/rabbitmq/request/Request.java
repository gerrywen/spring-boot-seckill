package com.gerrywen.seckill.third.rabbitmq.request;

/**
 * description: 该接口封装了请求的方法，实现类来实现具体的业务逻辑
 *
 * @author wenguoli
 * @date 2020/3/5 8:47
 */
public interface Request {
    /**
     * 处理业务
     */
    void process();
}
