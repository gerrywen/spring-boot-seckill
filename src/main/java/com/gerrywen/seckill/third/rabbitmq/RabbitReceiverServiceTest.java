package com.gerrywen.seckill.third.rabbitmq;

import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.listen.AbstractMessageHandler;
import com.gerrywen.seckill.third.rabbitmq.receiver.AbstractReceiverHandler;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 9:15
 */
@Slf4j
@Service
public class RabbitReceiverServiceTest extends AbstractReceiverHandler<String> {

    @PostConstruct
    public void init() {
        this.routingKey(QueueEnum.DIRECT_MODE_QUEUE_ONE.getRouteKey())
                .queue(QueueEnum.DIRECT_MODE_QUEUE_ONE.getName())
                .exchange(QueueEnum.DIRECT_MODE_QUEUE_ONE.getExchange())
                .registerQueue();
    }

    @Override
    public AbstractMessageHandler<String> messageHandler() {
        return new AbstractMessageHandler<String>() {

            @Override
            public boolean handleMessage(String message, Channel channel) {
                log.info("Test 接收消息：{}", message);
                return true;
            }
        };
    }
}
