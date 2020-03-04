package com.gerrywen.seckill.third.rabbitmq.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * description: 自定义rabbitmq队列
 *
 * @author wenguoli
 * @date 2020/3/4 17:42
 */
@Data
public class MqQueue {
    /**
     * 队列名称
     */
    @NotNull(message = "队列名称不能为空")
    private String name;
    /**
     * 是否持久化
     * 持久化会存盘，服务器重启时不会丢失相关信息
     */
    private Boolean durable;
    /**
     * 是否排他
     * 如果是排他，则该队列对首次声明他的连接有效，并在连接断开时自动删除
     * 注意：
     * 1. 同一个连接的其他的Channel是可以连接该排他队列的
     * 2. 首次是说其他连接就不同创建同名的排他队列
     * 适用于一个客户端同时发送和读取消息
     */
    private Boolean exclusive;
    /**
     * 是否自动删除
     * 自动删除的前提是至少有一个队列或者交换机与这个交互机绑定，之后所有与这个交换机绑定的队列或者交换机都与此解绑
     */
    private Boolean autoDelete;
    /**
     * 结构化参数
     * x-message-ttl、x-expires等
     */
    private Map<String, Object> arguments;

    public MqQueue name(String name) {
        this.name = name;
        return this;
    }

    public MqQueue durable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public MqQueue exclusive(boolean exclusive) {
        this.exclusive = exclusive;
        return this;
    }

    public MqQueue autoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    public MqQueue arguments(Map<String, Object> arguments) {
        this.arguments = arguments;
        return this;
    }
}
