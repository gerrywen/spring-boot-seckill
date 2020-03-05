package com.gerrywen.seckill.third.rabbitmq.sender;

import com.gerrywen.seckill.third.rabbitmq.bean.ModeExchange;
import com.gerrywen.seckill.third.rabbitmq.bean.MqQueue;
import com.gerrywen.seckill.third.rabbitmq.declare.AmBindDeclare;
import com.gerrywen.seckill.third.rabbitmq.declare.AmExchangeDeclare;
import com.gerrywen.seckill.third.rabbitmq.declare.AmQueueDeclare;
import com.gerrywen.seckill.third.rabbitmq.enums.ModeExchangeEnum;
import com.gerrywen.seckill.third.rabbitmq.exception.MqException;
import com.gerrywen.seckill.third.rabbitmq.message.MqMessage;
import com.gerrywen.seckill.third.rabbitmq.properties.RabbitProperties;
import com.gerrywen.seckill.third.rabbitmq.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 9:05
 */
public abstract class AbstractSendService<T> implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmBindDeclare amBindDeclare;
    @Autowired
    AmQueueDeclare amQueueDeclare;
    @Autowired
    AmExchangeDeclare amExchangeDeclare;

    /**
     * 简单的发送消息
     *
     * @param exchange
     * @param routingKey
     * @param queue
     * @param content
     */
    public void send(String exchange, String routingKey, String queue, T content) {
        if (StringUtils.isEmpty(exchange)) {
            throw new MqException("交换机不能为空");
        }
        if (StringUtils.isEmpty(routingKey)) {
            throw new MqException("路由键不能为空");
        }
        if (StringUtils.isEmpty(queue)) {
            throw new MqException("发送的队列不能为空");
        }
        if (StringUtils.isEmpty(content)) {
            throw new MqException("内容不能为空");
        }

        // 声明交换机
        amExchangeDeclare.declareExchange(new ModeExchange().name(exchange).type(ModeExchangeEnum.DIRECT));
        // 声明队列
        amQueueDeclare.declareQueue(new MqQueue().name(queue));
        // 路由键绑定队列交换机
        amBindDeclare.queueBind(queue, exchange, routingKey);

        this.send(exchange, routingKey, queue, content, null, UUIDUtils.generateUuid());
    }

    /**
     * 延迟队列
     *
     * @param exchange             交换机
     * @param routingKey           路由键
     * @param queue                队列
     * @param deadLetterExchange   死信交换机
     * @param deadLetterRoutingKey 死信路由键
     * @param content              发送的内容
     * @param expireTime           过期时间 单位:毫秒
     */
    public void send(String exchange, String routingKey, String queue, String deadLetterExchange, String deadLetterRoutingKey, T content, Long expireTime) {
        if (StringUtils.isEmpty(routingKey)) {
            throw new MqException("路由键不能为空");
        }
        if (StringUtils.isEmpty(exchange)) {
            throw new MqException("交换机不能为空");
        }
        if (StringUtils.isEmpty(queue)) {
            throw new MqException("队列不能为空");
        }
        if (StringUtils.isEmpty(deadLetterRoutingKey)) {
            throw new MqException("死信路由键不能为空");
        }
        if (StringUtils.isEmpty(deadLetterExchange)) {
            throw new MqException("死信交换机不能为空");
        }
        if (StringUtils.isEmpty(content)) {
            throw new MqException("内容不能为空");
        }

        // 声明交换机
        amExchangeDeclare.declareExchange(new ModeExchange().name(exchange).type(ModeExchangeEnum.DIRECT));
        // 声明队列 及 死信路由交换机属性
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", deadLetterExchange);
        arguments.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        // 声明队列
        amQueueDeclare.declareQueue(new MqQueue().name(queue).arguments(arguments));
        // 路由键绑定队列交换机
        amBindDeclare.queueBind(queue, exchange, routingKey);

        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置过期时间  毫秒
                message.getMessageProperties().setExpiration(String.valueOf(expireTime));
                return message;
            }
        };

        this.send(exchange, routingKey, queue, content, messagePostProcessor, UUIDUtils.generateUuid());
    }

    /**
     * 按照给定的交换机、路由键、发送内容、发送的自定义属性来发送消息
     *
     * @param exchange             交换机名称
     * @param routingKey           路由键
     * @param queue                队列
     * @param content              发送的内容
     * @param messagePostProcessor 发送消息自定义处理
     * @param messageId            消息ID
     */
    public void send(String exchange, String routingKey, String queue, T content, MessagePostProcessor messagePostProcessor, String messageId) {
        if (StringUtils.isEmpty(exchange)) {
            throw new MqException("交换机不能为空");
        }
        if (StringUtils.isEmpty(routingKey)) {
            throw new MqException("路由键不能为空");
        }
        if (StringUtils.isEmpty(content)) {
            throw new MqException("发送的内容不能为空");
        }
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(StringUtils.isEmpty(messageId) ? UUIDUtils.generateUuid() : messageId);
        MqMessage<T> mqMessage = new MqMessage<T>();
        mqMessage.setMessageBody(content);
        mqMessage.setMessageId(correlationData.getId());
        mqMessage.setExchangeName(exchange);
        mqMessage.setQueueName(queue);
        mqMessage.setRoutingKey(routingKey);
        if (StringUtils.isEmpty(messagePostProcessor)) {
            this.rabbitTemplate.convertAndSend(exchange, routingKey, mqMessage, correlationData);
        } else {
            this.rabbitTemplate.convertAndSend(exchange, routingKey, mqMessage, messagePostProcessor, correlationData);
        }
    }

    /**
     * 默认实现发送确认的处理方法
     * 子类需要重写该方法，实现自己的业务处理逻辑
     *
     * @param messageId 消息
     * @param ack
     * @param cause
     */
    public abstract void handleConfirmCallback(String messageId, boolean ack, String cause);

    /**
     * 默认实现发送匹配不上队列时 回调函数的处理
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param routingKey
     */
    public abstract void handleReturnCallback(Message message, int replyCode, String replyText,
                                              String routingKey);

    /**
     * 交换机如果根据自身的类型和路由键匹配上对应的队列时，是否调用returnCallback回调函数
     * true: 调用returnCallback回调函数
     * false： 不调用returnCallback回调函数 这样在匹配不上对应的队列时，会导致消息丢失
     */
    private Boolean mandatory = RabbitProperties.mandatory;

    @PostConstruct
    public final void init() {
        this.logger.info("sendservice 初始化...... ");
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 确认后回调方法
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public final void confirm(CorrelationData correlationData, boolean ack, String cause) {
        this.logger.info("confirm-----correlationData:" + correlationData.toString() + "---ack:" + ack + "----cause:" + cause);
        // TODO 记录日志
        this.handleConfirmCallback(correlationData.getId(), ack, cause);
    }

    /**
     * 失败后回调方法
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public final void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        this.logger.info("return-----message:" + message.toString() + "---replyCode:" + replyCode + "----replyText:" + replyText + "----exchange:" + exchange + "----routingKey:" + routingKey);
        // TODO 记录日志
        this.handleReturnCallback(message, replyCode, replyText, routingKey);
    }

}
