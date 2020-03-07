package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.GoodsService;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.third.redis.constant.GoodsKey;
import com.gerrywen.seckill.third.redis.enums.CtimsModelEnum;
import com.gerrywen.seckill.vo.GoodsDetailVO;
import com.gerrywen.seckill.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * program: spring-boot-seckill->GoodsController
 * description:
 * author: gerry
 * created: 2020-03-05 21:51
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;


    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        //取缓存
        String html = redisService.get(CtimsModelEnum.CTIMS_GOODS_CAP, GoodsKey.GOODS_LIST_KEY_PREFIX);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }

        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(CtimsModelEnum.CTIMS_GOODS_CAP, GoodsKey.GOODS_LIST_KEY_PREFIX, html, 7200);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                          @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        //取缓存
        String html = redisService.get(CtimsModelEnum.CTIMS_GOODS_CAP,
                GoodsKey.GOODS_DETAIL_KEY_PREFIX + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //手动渲染
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus;
        int remainSeconds;
        if (now < startAt) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        IWebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(CtimsModelEnum.CTIMS_GOODS_CAP,
                    GoodsKey.GOODS_DETAIL_KEY_PREFIX + goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                        @PathVariable("goodsId") long goodsId) {
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus;
        int remainSeconds;
        if (now < startAt) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVO vo = new GoodsDetailVO();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
