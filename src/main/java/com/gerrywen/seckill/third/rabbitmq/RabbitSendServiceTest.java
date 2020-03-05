package com.gerrywen.seckill.third.rabbitmq;

import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.sender.AbstractSendService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 9:15
 */
@Service
public class RabbitSendServiceTest extends AbstractSendService {

    public void send() {
        this.send(QueueEnum.DIRECT_MODE_QUEUE_ONE.getExchange(),
                QueueEnum.DIRECT_MODE_QUEUE_ONE.getRouteKey(), QueueEnum.DIRECT_MODE_QUEUE_ONE.getName(), "send test message");
    }

    @Override
    public void handleConfirmCallback(String messageId, boolean ack, String cause) {
        if(!ack){
            logger.info("打印异常处理....");
        }
    }

    @Override
    public void handleReturnCallback(Message message, int replyCode, String replyText, String routingKey) {

    }
}
