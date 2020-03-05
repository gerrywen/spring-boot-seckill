package com.gerrywen.seckill.third.rabbitmq.util;

import java.util.UUID;

/**
 * description: uuid 操作类
 *
 * @author wenguoli
 * @date 2020/3/5 8:53
 */
public class UUIDUtils {
    /**
     * 生成随机的uuid
     *
     * @return
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
