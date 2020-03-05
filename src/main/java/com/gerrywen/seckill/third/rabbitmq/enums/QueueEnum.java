package com.gerrywen.seckill.third.rabbitmq.enums;

import lombok.Getter;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/4 17:34
 */
@Getter
public enum QueueEnum {

    /**
     * 直接模式1
     */
    DIRECT_MODE_QUEUE_ONE("queue.direct.1", "queue.direct.1", "queue.direct.1"),
    /**
     * 队列2
     */
    QUEUE_TWO("queue.2", "queue.2", "queue.2");



    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
