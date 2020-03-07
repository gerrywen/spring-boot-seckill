package com.gerrywen.seckill.service;

import com.gerrywen.seckill.dao.OrderDao;
import com.gerrywen.seckill.domain.MiaoshaOrder;
import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.domain.OrderInfo;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.third.redis.constant.OrderKey;
import com.gerrywen.seckill.third.redis.enums.CtimsModelEnum;
import com.gerrywen.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * program: spring-boot-seckill->OrderService
 * description:
 * author: gerry
 * created: 2020-03-07 09:07
 **/
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(CtimsModelEnum.CTIMS_ORDER_CAP,
                OrderKey.ORDER_MIAOSHA_UID_GID_KEY_PREFIX + userId + "_" + goodsId, MiaoshaOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVO goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        redisService.set(CtimsModelEnum.CTIMS_ORDER_CAP,
                OrderKey.ORDER_MIAOSHA_UID_GID_KEY_PREFIX + user.getId() + "_" + goods.getId(), miaoshaOrder);

        return orderInfo;
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteMiaoshaOrders();
    }
}
