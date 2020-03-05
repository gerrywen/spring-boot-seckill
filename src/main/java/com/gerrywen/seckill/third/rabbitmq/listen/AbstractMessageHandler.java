package com.gerrywen.seckill.third.rabbitmq.listen;

import com.alibaba.fastjson.JSON;
import com.gerrywen.seckill.third.rabbitmq.message.MqMessage;
import com.google.common.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * description:
 * 实现 ChannelAwareMessageListener接口 重写onMessage方法来实现业务的处理
 *
 * @author wenguoli
 * @date 2020/3/5 8:56
 */
@Component
public abstract class AbstractMessageHandler<T> implements ChannelAwareMessageListener {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    //    @Value("${spring.message.queue.retryTimes:5}")
    private Integer retryTimes = 5;

    /**
     * 用户自定义消息处理
     *
     * @param message 消息
     * @param channel 交换机
     * @return
     */
    public abstract boolean handleMessage(T message, Channel channel);

    /**
     * ack
     */
    private ConcurrentHashMap<String, AcknowledgeMode> ackMap = new ConcurrentHashMap<>(8);

    /**
     * 消息处理
     *
     * @param message 消息体
     * @param channel channel通道
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        this.logger.info("接收到发送的消息.......");
        // 业务处理是否成功
        boolean handleResult = false;
        // 消息处理标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 获取消费的队列名
        String queue = message.getMessageProperties().getConsumerQueue();

        MqMessage<T> mqMessage = null;
        // TODO 进行自己的业务处理 比如记录日志
        try {
            String msg = new String(message.getBody());
            TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
            };
            Class<T> classType = (Class<T>) typeToken.getRawType();
            mqMessage = JSON.parseObject(msg, MqMessage.class);
            // 自定义业务处理
            handleResult = this.handleMessage(JSON.parseObject(JSON.toJSONString(mqMessage.getMessageBody()), classType), channel);
        } catch (Exception e) {
            if (this.logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        // TODO 如果消息处理失败，处理失败的采取措施， 确保消息不丢失
        this.onMessageCompleted(mqMessage, queue, channel, deliveryTag, handleResult);
    }

    /**
     * 消息处理结束后进行复处理
     *
     * @param mqMessage    消息实体
     * @param queue
     * @param channel
     * @param deliveryTag
     * @param handleResult 业务处理是否成功
     */
    private void onMessageCompleted(MqMessage<T> mqMessage, String queue, Channel channel, long deliveryTag, boolean handleResult) {
        this.logger.info("消息：" + mqMessage.toString() + "处理完成，等待事务提交和状态更新");
        if (!handleResult) {
            // TODO 业务处理失败，需要更新状态  先简单记录日志
            this.logger.error("消息处理失败:{}", mqMessage.toString());
            return;
        }
        AcknowledgeMode ack = this.ackMap.get(queue);
        if (ack.isManual()) {
            //重试5次
            int retryTimes = 5;
            //进行消息
            RetryTemplate oRetryTemplate = new RetryTemplate();
            SimpleRetryPolicy oRetryPolicy = new SimpleRetryPolicy();
            oRetryPolicy.setMaxAttempts(retryTimes);
            oRetryTemplate.setRetryPolicy(oRetryPolicy);
            try {
                // obj为doWithRetry的返回结果，可以为任意类型
                Integer result = oRetryTemplate.execute(new RetryCallback<Integer, Exception>() {
                    int count = 0;

                    @Override
                    public Integer doWithRetry(RetryContext context) throws Exception {//开始重试
                        channel.basicAck(deliveryTag, false);
                        AbstractMessageHandler.this.logger.info("消息" + mqMessage.toString() + "已签收");
                        return ++this.count;
                    }
                }, new RecoveryCallback<Integer>() {
                    @Override
                    public Integer recover(RetryContext context) throws Exception { //重试多次后都失败了
                        AbstractMessageHandler.this.logger.info("消息" + mqMessage.toString() + "签收失败");
                        return Integer.MAX_VALUE;
                    }
                });

                if (result.intValue() <= retryTimes) {
                    //消息签收成功 更改状态
                } else {
                    //MQ服务器或网络出现问题，签收失败 更改状态
                }
            } catch (Exception e) {
                this.logger.error("消息" + mqMessage.toString() + "签收出现异常：" + e.getMessage());
            }
        } else {
            this.logger.info("消息自动签收");
        }

    }

    /**
     * @param ack
     * @Title: setAck
     * @date: 2018年9月14日 上午11:17:41
     * @Description: 注入消息签收模式
     */
    public final void setAck(String queue, AcknowledgeMode ack) {
        this.ackMap.put(queue, ack);
        this.logger.info("注入队列 " + queue + " 消息签收模式: " + ack.name());
    }
}