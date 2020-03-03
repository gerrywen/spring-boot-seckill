package com.gerrywen.seckill.third.redis.key;

/**
 * program: spring-boot-seckill->KeyPrefix
 * description: 键前缀
 * author: gerry
 * created: 2020-03-03 22:02
 **/
public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
