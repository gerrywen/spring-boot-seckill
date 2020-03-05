package com.gerrywen.seckill.util;

import java.util.UUID;

/**
 * program: spring-boot-seckill->UUIDUtil
 * description:
 * author: gerry
 * created: 2020-03-05 20:56
 **/
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
