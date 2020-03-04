package com.gerrywen.seckill.third.rabbitmq.bean;

import com.gerrywen.seckill.third.rabbitmq.enums.ModeExchangeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * description: rabbitmq交换机
 *
 * @author wenguoli
 * @date 2020/3/4 17:29
 */
@Data
public class ModeExchange {
    /**
     * 默认的DIRECT类型的交换机
     */
    public static final String DEFAULT_DIRECT_EXCHANGE = "amqp.direct";

    /**
     * 默认的FANOUT类型的交换机
     */
    public static final String DEFAULT_FANOUT_EXCHANGE = "amqp.fanout";

    /**
     * 默认的TOPIC类型的交换机
     */
    public static final String DEFAULT_TOPIC_EXCHANGE = "amqp.topic";

    /**
     * 默认的DELAY类型的交换机
     */
    public static final String DEFAULT_DELAY_EXCHANGE = "amqp.delay";

    /**
     * 默认的HEADERS类型的交换机
     */
    public static final String DEFAULT_HEADERS_EXCHANGE = "amqp.headers";


    /**
     * 交换机的名称
     */
    @NotNull(message = "交换机名称不能为空")
    private String name;

    /**
     * 交换机的类型
     */
    @NotNull(message = "交换机类型不能为空")
    private ModeExchangeEnum type;
    /**
     * 是否持久化
     * 持久化可以将交换机存盘，在服务器重启的时候不会丢失相关的信息
     * 默认是开启持久化
     */
    private boolean durable = Boolean.TRUE;
    /**
     * 是否自动删除
     * 自动删除的前提是至少有一个队列或者交换机与这个交互机绑定，之后所有与这个交换机绑定的队列或者交换机都与此解绑
     */
    private boolean autoDelete;
    /**
     * 自定义属性参数
     * 比如：alternate-exchange
     */
    private Map<String, Object> arguments;

    public ModeExchange name(String name) {
        this.name = name;
        return this;
    }

    public ModeExchange type(ModeExchangeEnum type) {
        this.type = type;
        return this;
    }

    public ModeExchange durable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public ModeExchange autoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    public ModeExchange arguments(Map<String, Object> arguments) {
        this.arguments = arguments;
        return this;
    }


}
