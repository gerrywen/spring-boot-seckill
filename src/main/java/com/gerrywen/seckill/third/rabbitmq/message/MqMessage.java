package com.gerrywen.seckill.third.rabbitmq.message;

import lombok.Data;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 8:58
 */
@Data
public class MqMessage<T> {
    /**
     * 交换机名称
     */
    private String exchangeName;
    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 消息Id
     */
    private String messageId;
    /**
     * 消息体
     */
    private T messageBody;
    /**
     * 路由键
     */
    private String routingKey;
    /**
     * 消息优先级
     */
    private String messagePriority;
    /**
     * 补偿次数
     */
    private Integer tryNum;


}
