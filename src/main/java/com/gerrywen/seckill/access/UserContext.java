package com.gerrywen.seckill.access;

import com.gerrywen.seckill.domain.MiaoshaUser;

/**
 * program: spring-boot-seckill->UserContext
 * description:
 * author: gerry
 * created: 2020-03-07 11:00
 **/
public class UserContext {
    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }
}
