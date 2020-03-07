package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.access.AccessLimit;
import com.gerrywen.seckill.domain.MiaoshaOrder;
import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.domain.OrderInfo;
import com.gerrywen.seckill.result.CodeMsg;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.GoodsService;
import com.gerrywen.seckill.service.MiaoshaService;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.service.OrderService;
import com.gerrywen.seckill.service.mq.MiaoshaMessage;
import com.gerrywen.seckill.service.mq.MiaoshaMessageSendService;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.third.redis.constant.GoodsKey;
import com.gerrywen.seckill.third.redis.constant.MiaoshaKey;
import com.gerrywen.seckill.third.redis.constant.OrderKey;
import com.gerrywen.seckill.third.redis.enums.CtimsModelEnum;
import com.gerrywen.seckill.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * program: spring-boot-seckill->MiaoshaController
 * description:
 * author: gerry
 * created: 2020-03-07 09:59
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

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

    @Autowired
    MiaoshaMessageSendService miaoshaMessageSendService;

    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     */
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVO goods : goodsList) {
            redisService.set(CtimsModelEnum.CTIMS_GOODS_CAP,
                    GoodsKey.GOODS_STOCK_KEY_PREFIX + "" + goods.getId(), goods.getStockCount(), 7200);
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        for (GoodsVO goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(CtimsModelEnum.CTIMS_GOODS_CAP,
                    GoodsKey.GOODS_STOCK_KEY_PREFIX + "" + goods.getId(), 10, 7200);
            localOverMap.put(goods.getId(), false);
        }
        redisService.del(CtimsModelEnum.CTIMS_ORDER_CAP, OrderKey.ORDER_MIAOSHA_UID_GID_KEY_PREFIX);
        redisService.del(CtimsModelEnum.CTIMS_GOODS_CAP, MiaoshaKey.MIAOSHA_GOODS_OVER_KEY_PREFIX);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }


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

    /**
     * QPS:1306
     * 5000 * 10
     * QPS: 2114
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId,
                                   @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        String s = redisService.get(CtimsModelEnum.CTIMS_GOODS_CAP, GoodsKey.GOODS_STOCK_KEY_PREFIX + "" + goodsId);
        long stock = Long.parseLong(s);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        redisService.set(CtimsModelEnum.CTIMS_GOODS_CAP,
                GoodsKey.GOODS_STOCK_KEY_PREFIX + "" + goodsId, stock -1, 7200);
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        miaoshaMessageSendService.send(mm);
        return Result.success(0);//排队中

    }


    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode
    ) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
