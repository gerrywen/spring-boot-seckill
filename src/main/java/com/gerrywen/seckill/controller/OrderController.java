package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.domain.OrderInfo;
import com.gerrywen.seckill.result.CodeMsg;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.GoodsService;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.service.OrderService;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.vo.GoodsVO;
import com.gerrywen.seckill.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: spring-boot-seckill->OrderController
 * description:
 * author: gerry
 * created: 2020-03-07 09:56
 **/
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(Model model, MiaoshaUser user,
                                      @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
