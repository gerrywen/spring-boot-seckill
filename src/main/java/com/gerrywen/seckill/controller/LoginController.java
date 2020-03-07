package com.gerrywen.seckill.controller;

import com.gerrywen.seckill.dto.LoginDTO;
import com.gerrywen.seckill.result.Result;
import com.gerrywen.seckill.service.MiaoshaUserService;
import com.gerrywen.seckill.third.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/3 14:40
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("")
    public String defaultLogin() {
        return "login";
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginDTO loginDTO) {
        log.info(loginDTO.toString());
        //登录
        String token = userService.login(response, loginDTO);
        return Result.success(token);
    }
}
