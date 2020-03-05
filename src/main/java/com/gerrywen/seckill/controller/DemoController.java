package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.domain.User;
import com.gerrywen.seckill.result.CodeMsg;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.UserService;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.third.redis.constant.UserKey;
import com.gerrywen.seckill.third.redis.enums.CtimsModelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: spring-boot-seckill->DemoController
 * description: 例子
 * author: gerry
 * created: 2020-03-03 20:01
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;


    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> home() {
        return Result.success("Hello，world");
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/hello/themaleaf")
    public String themaleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }


    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_ID_KEY_PREFIX + 1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_ID_KEY_PREFIX + 1, user);//UserKey:id1
        return Result.success(true);
    }


}
