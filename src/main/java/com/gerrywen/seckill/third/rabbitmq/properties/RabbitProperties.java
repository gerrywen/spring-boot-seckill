package com.gerrywen.seckill.third.rabbitmq.properties;



/**
 * description:
 * <p>
 * 参数配置
 *
 * @author wenguoli
 * @date 2020/3/5 10:02
 */
public interface RabbitProperties {

    Boolean mandatory = false;

    Boolean isAck = false;

    Integer retryTimes = 5;


    /**
     * 核心线程数
     */
    Integer corePoolSize = 10;
    /**
     * 线程池最大线程数
     */
    Integer maximumPoolSize = 20;

    /**
     * 线程最大存活时间
     */
    Long keepAliveTime = 60L;


}
