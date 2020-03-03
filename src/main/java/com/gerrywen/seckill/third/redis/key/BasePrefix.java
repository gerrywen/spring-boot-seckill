package com.gerrywen.seckill.third.redis.key;

/**
 * program: spring-boot-seckill->BasePrefix
 * description: 基类前缀
 * author: gerry
 * created: 2020-03-03 22:02
 **/
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
