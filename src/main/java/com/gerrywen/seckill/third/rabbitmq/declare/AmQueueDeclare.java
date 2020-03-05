package com.gerrywen.seckill.third.rabbitmq.declare;

import com.gerrywen.seckill.third.rabbitmq.bean.MqQueue;
import com.gerrywen.seckill.third.rabbitmq.exception.MqException;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * description: 队列
 *
 * @author wenguoli
 * @date 2020/3/5 9:02
 */
@Component
public class AmQueueDeclare extends AbstractDeclare {
    /**
     * 声明队列
     * 向rabbitMQ服务器声明一个队列
     *
     * @param mqQueue
     * @return
     */
    public Queue declareQueue(MqQueue mqQueue) {
        this.logger.info("the parameter queue is : " + mqQueue.toString());

        super.validate(mqQueue);

        Queue queue = new Queue(mqQueue.getName(), Objects.isNull(mqQueue.getDurable()) ? true : mqQueue.getDurable(), Objects.isNull(mqQueue.getExclusive()) ? false : mqQueue.getExclusive(), Objects.isNull(mqQueue.getAutoDelete()) ? false : mqQueue.getAutoDelete(), mqQueue.getArguments());

        this.logger.info("declare queue is : " + queue.toString());

        super.rabbitAdmin.declareQueue(queue);

        this.logger.info("declare queue success");
        return queue;
    }

    /**
     * 清空队列中的消息
     *
     * @param queueName
     * @return 清楚队列中的消息的个数
     */
    public int purgeQueue(String queueName) {
        if (StringUtils.isEmpty(queueName)) {
            throw new MqException("参数不能为空");
        }
        this.logger.info("purge queue is : " + queueName);
        super.rabbitAdmin.purgeQueue(queueName, true);
        return 0;
    }

    /**
     * 判断指定的队列是否存在
     * 1. 如果存在则返回该队列
     * 2. 如果不存在则返回null
     *
     * @param queueName
     * @return true 存在， false 不存在
     */
    public boolean isQueueExist(String queueName) {
        if (StringUtils.isEmpty(queueName)) {
            throw new MqException("参数不能为空");
        }

        this.logger.info("isQueueExist queue is : " + queueName);

        String isExist = super.rabbitAdmin.getRabbitTemplate().execute((channel -> {
            try {
                AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueName);
                return declareOk.getQueue();
            } catch (Exception e) {
                if (this.logger.isDebugEnabled()) {
                    throw new MqException(e.getMessage());
                }
                return null;
            }
        }));

        this.logger.info("the queue " + queueName + " is exist : " + isExist);
        return StringUtils.isEmpty(isExist) ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 从rabbitMQ服务器中删除指定的队列
     *
     * @param queueName
     * @return
     */
    public boolean deleteQueue(String queueName) {
        this.logger.info("delete queue is :" + queueName);

        if (StringUtils.isEmpty(queueName)) {
            throw new MqException("参数不能为空");
        }

        return super.rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 从rabbitMQ服务器中删除指定的队列
     *
     * @param queueName 队列名称
     * @param unused    队列是否在使用，如果设置为true则该队列只能在没有被使用的情况下才能删除
     * @param empty     队列是否为空，如果设置为true则该队列只能在该队列没有消息时才会被删除
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        this.logger.info("delete queue is : { queueName : '" + queueName
                + "' , unused: '" + unused + "' , empty:'" + empty + "'}");

        if (StringUtils.isEmpty(queueName)) {
            throw new MqException("参数不能为空");
        }

        super.rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

    /**
     * 自定义的校验
     *
     * @param object
     */
    @Override
    public void definedValidate(Object object) {

    }
}
