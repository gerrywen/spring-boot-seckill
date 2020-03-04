package com.gerrywen.seckill.third.rabbitmq.constants;

/**
 * description:  RabbitMQ常量池
 *
 * @author wenguoli
 * @date 2020/3/4 9:50
 */
public interface RabbitConsts {

    /**
     * RabbitMQ的三种模式-----直接模式（Direct）
     * 我们需要将消息发给唯一一个节点时使用这种模式，这是简单的一种形式。
     *
     * 直接模式1
     */
    String DIRECT_MODE_QUEUE_ONE = "queue.direct.1";

    /**
     * 队列2
     */
    String QUEUE_TWO = "queue.2";

    /**
     * 队列3
     */
    String QUEUE_THREE = "3.queue";

    /**
     * RabbitMQ的三种模式-----分列模式（Fanout）
     * 当我们需要将消息一次发给多个队列时，需要使用这种模式
     *
     * 分列模式
     */
    String FANOUT_MODE_QUEUE = "fanout.mode";

    /**
     * RabbitMQ的三种模式-----主题模式(Topic)
     * 任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上
     *
     * 主题模式
     */
    String TOPIC_MODE_QUEUE = "topic.mode";

    /**
     * 路由1
     */
    String TOPIC_ROUTING_KEY_ONE = "queue.#";

    /**
     * 路由2
     */
    String TOPIC_ROUTING_KEY_TWO = "*.queue";

    /**
     * 路由3
     */
    String TOPIC_ROUTING_KEY_THREE = "3.queue";

    /**
     * 延迟队列
     */
    String DELAY_QUEUE = "delay.queue";

    /**
     * 延迟队列交换器
     */
    String DELAY_MODE_QUEUE = "delay.mode";
}
