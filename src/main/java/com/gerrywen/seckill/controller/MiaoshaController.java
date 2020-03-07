package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.domain.MiaoshaOrder;
import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.domain.OrderInfo;
import com.gerrywen.seckill.result.CodeMsg;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.GoodsService;
import com.gerrywen.seckill.service.MiaoshaService;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.service.OrderService;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: spring-boot-seckill->MiaoshaController
 * description:
 * author: gerry
 * created: 2020-03-07 09:59
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品，req1 req2
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);
    }
}
