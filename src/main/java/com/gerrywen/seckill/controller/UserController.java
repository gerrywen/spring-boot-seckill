package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.third.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: spring-boot-seckill->UserController
 * description:
 * author: gerry
 * created: 2020-03-07 09:37
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }

}
