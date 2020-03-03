package com.gerrywen.seckill.redis.key;

/**
 * program: spring-boot-seckill->UserKey
 * description: 用户键
 * author: gerry
 * created: 2020-03-03 22:03
 **/
public class UserKey extends BasePrefix {
    public UserKey(String prefix) {
        super(prefix);
    }

    /**
     * 用户ID
     */
    public static UserKey getById = new UserKey("id");
    /**
     * 用户名称
     */
    public static UserKey getByName = new UserKey("name");
}
