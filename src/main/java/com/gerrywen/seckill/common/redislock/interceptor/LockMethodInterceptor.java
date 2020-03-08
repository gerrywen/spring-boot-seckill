package com.gerrywen.seckill.common.redislock.interceptor;

import com.gerrywen.seckill.common.redislock.annotation.CacheLock;
import com.gerrywen.seckill.common.redislock.component.CacheKeyGenerator;
import com.gerrywen.seckill.common.redislock.component.LockKeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Method;

/**
 * program: spring-boot-seckill->LockMethodInterceptor
 * description: Redis方案实现分布式锁
 * author: gerry
 * created: 2020-03-08 20:50
 **/
@Aspect
@Configuration
public class LockMethodInterceptor {

    private final StringRedisTemplate lockRedisTemplate;
    private final CacheKeyGenerator cacheKeyGenerator;

    @Autowired
    public LockMethodInterceptor(StringRedisTemplate lockRedisTemplate) {
        this.lockRedisTemplate = lockRedisTemplate;
        this.cacheKeyGenerator =  new LockKeyGenerator();
    }

    @Around("execution(public * *(..)) && @annotation(com.gerrywen.seckill.common.redislock.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key can't be null...");
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjp);
        try {
            //key不存在才能设置成功
            final Boolean success = lockRedisTemplate.opsForValue().setIfAbsent(lockKey, "");
            if (success != null && success) {
                lockRedisTemplate.expire(lockKey, lock.expire(), lock.timeUnit());
            } else {
                //按理来说 我们应该抛出一个自定义的 CacheLockException 异常;
                throw new RuntimeException("请勿重复请求");
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }

        } finally {
            //如果演示的话需要注释该代码;实际应该放开
//             lockRedisTemplate.delete(lockKey);
        }

    }
}
