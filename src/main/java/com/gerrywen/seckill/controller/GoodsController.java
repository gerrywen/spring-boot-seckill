package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.third.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
