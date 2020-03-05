package com.gerrywen.seckill.third.rabbitmq.receiver;

import com.gerrywen.seckill.third.rabbitmq.bean.ModeExchange;
import com.gerrywen.seckill.third.rabbitmq.bean.MqQueue;
import com.gerrywen.seckill.third.rabbitmq.declare.AmBindDeclare;
import com.gerrywen.seckill.third.rabbitmq.declare.AmExchangeDeclare;
import com.gerrywen.seckill.third.rabbitmq.declare.AmQueueDeclare;
import com.gerrywen.seckill.third.rabbitmq.enums.ModeExchangeEnum;
import com.gerrywen.seckill.third.rabbitmq.listen.AbstractMessageHandler;
import com.gerrywen.seckill.third.rabbitmq.listen.MessageListen;
import com.gerrywen.seckill.third.rabbitmq.properties.RabbitProperties;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * description:  注册队列并且设置监听
 *
 * @author wenguoli
 * @date 2020/3/5 9:08
 */
@Data
public abstract class AbstractReceiverHandler<T> {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AmBindDeclare amBindDeclare;
    @Autowired
    AmQueueDeclare amQueueDeclare;
    @Autowired
    AmExchangeDeclare amExchangeDeclare;
    @Autowired
    MessageListen messageListen;

    private Boolean isAck = RabbitProperties.isAck;

    /**
     * 子类提供自定义的消息监听
     *
     * @return
     */
    public abstract AbstractMessageHandler<T> messageHandler();

    /**
     * 实例化队列名
     *
     * @param queue
     * @return
     */
    public AbstractReceiverHandler queue(String queue) {
        this.queue = queue;
        return this;
    }

    /**
     * 实例化交换机
     *
     * @param exchange
     * @return
     */
    public AbstractReceiverHandler exchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    /**
     * 实例化路由键
     *
     * @param routingKey
     * @return
     */
    public AbstractReceiverHandler routingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    /**
     * 实例化结构化属性
     *
     * @param properties
     * @return
     */
    public AbstractReceiverHandler properties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    /**
     * 队列名
     */
    private String queue;
    /**
     * 交换机 默认是 amq.direct 交换机
     */
    private String exchange = ModeExchange.DEFAULT_DIRECT_EXCHANGE;
    /**
     * 路由键 默认是队列名
     */
    private String routingKey = this.getQueue();
    /**
     * 结构化属性
     */
    private Map<String, Object> properties;

    public String getRoutingKey() {
        if (StringUtils.isEmpty(this.routingKey)) {
            return this.getQueue();
        }
        return this.routingKey;
    }

    /**
     * 注册队列，并且监听队列
     *
     * @return
     */
    public boolean registerQueue() {
        this.amExchangeDeclare.declareExchange(new ModeExchange().name(this.exchange).type(ModeExchangeEnum.DIRECT));
        this.amQueueDeclare.declareQueue(new MqQueue().name(this.queue).arguments(properties));
        boolean tag = this.amBindDeclare.bind(this.queue, Binding.DestinationType.QUEUE, this.exchange, this.getRoutingKey(), this.properties);
        if (tag) {
            try {
                this.messageListen.addMessageLister(this.queue, this.messageHandler(), this.isAck);
                return Boolean.TRUE;
            } catch (Exception e) {
                if (this.logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return Boolean.FALSE;
            }

        }
        return tag;
    }
}
