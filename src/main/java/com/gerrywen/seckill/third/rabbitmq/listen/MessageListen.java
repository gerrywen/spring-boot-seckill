package com.gerrywen.seckill.third.rabbitmq.listen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description: 队列设置监听基类
 *
 * @author wenguoli
 * @date 2020/3/5 8:56
 */
@Component
public class MessageListen<T> {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ConnectionFactory connectionFactory;

    @Autowired
    public MessageListen(ConnectionFactory connectionFactory) {
        // 使用构造器注入的方法，可以明确成员变量的加载顺序。
        this.connectionFactory = connectionFactory;
    }

    /**
     * 在容器中加入消息监听
     *
     * @param queue
     * @param messageHandler
     * @param isAck
     * @throws Exception
     */
    public void addMessageLister(String queue, AbstractMessageHandler<T> messageHandler, boolean isAck) throws Exception {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(this.connectionFactory);
        container.setQueueNames(queue);
        AcknowledgeMode ack = AcknowledgeMode.NONE;
        if (isAck) {
            ack = AcknowledgeMode.MANUAL;
        }
        messageHandler.setAck(queue, ack);
        container.setAcknowledgeMode(ack);
        MessageListenerAdapter adapter = new MessageListenerAdapter(messageHandler);
        container.setMessageListener(adapter);
        container.start();
        logger.info("------ 已成功监听异步消息触发通知队列：" + queue + " ------");
    }
}
