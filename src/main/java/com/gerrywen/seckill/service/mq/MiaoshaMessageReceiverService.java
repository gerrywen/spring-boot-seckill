package com.gerrywen.seckill.service.mq;

import com.gerrywen.seckill.domain.MiaoshaOrder;
import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.service.GoodsService;
import com.gerrywen.seckill.service.MiaoshaService;
import com.gerrywen.seckill.service.OrderService;
import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.listen.AbstractMessageHandler;
import com.gerrywen.seckill.third.rabbitmq.receiver.AbstractReceiverHandler;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.vo.GoodsVO;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * program: spring-boot-seckill->MiaoshaMessageReceiverService
 * description:
 * author: gerry
 * created: 2020-03-07 11:37
 **/
@Service
public class MiaoshaMessageReceiverService extends AbstractReceiverHandler<MiaoshaMessage> {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @PostConstruct
    public void init() {
        this.routingKey(QueueEnum.MIAOSHA_MODE_QUEUE.getRouteKey())
                .queue(QueueEnum.MIAOSHA_MODE_QUEUE.getName())
                .exchange(QueueEnum.MIAOSHA_MODE_QUEUE.getExchange())
                .registerQueue();
    }

    @Override
    public AbstractMessageHandler<MiaoshaMessage> messageHandler() {
        return new AbstractMessageHandler<MiaoshaMessage>() {

            @Override
            public boolean handleMessage(MiaoshaMessage message, Channel channel) {
                logger.info("Message 接收消息：{}", message);
                long goodsId = message.getGoodsId();
                MiaoshaUser user = message.getUser();
                GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
                int stock = goods.getStockCount();
                if (stock <= 0) {
                    return true;
                }
                //判断是否已经秒杀到了
                MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
                if (order != null) {
                    return true;
                }
                //减库存 下订单 写入秒杀订单
                miaoshaService.miaosha(user, goods);
                return true;
            }
        };
    }
}
