package com.gerrywen.seckill.third.redis.factory;

import com.gerrywen.seckill.third.redis.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * program: spring-boot-seckill->RedisPoolFactory
 * description: 工厂类
 * author: gerry
 * created: 2020-03-03 21:51
 **/
@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(50);
        poolConfig.setMinIdle(8);//设置最小空闲数
        poolConfig.setMaxWaitMillis(10000);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        //Idle时进行连接扫描
        poolConfig.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        poolConfig.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        poolConfig.setMinEvictableIdleTimeMillis(60000);

        return new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeOut() * 1000, redisConfig.getPassword(), redisConfig.getDatabase());
    }
}
