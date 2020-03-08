package com.gerrywen.seckill.common.redislock.component;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * program: spring-boot-seckill->CacheKeyGenerator
 * description: key生成器
 * author: gerry
 * created: 2020-03-08 20:32
 **/
public interface CacheKeyGenerator {


    /**
     * 获取AOP参数,生成指定缓存Key
     * @param pjp
     * @return 缓存KEY
     */
    String getLockKey(ProceedingJoinPoint pjp);
}
