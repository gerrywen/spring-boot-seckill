package com.gerrywen.seckill.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/3 14:40
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @GetMapping("")
    public String test() {
        return "小张，瞧啥瞧！！！";
    }
}
