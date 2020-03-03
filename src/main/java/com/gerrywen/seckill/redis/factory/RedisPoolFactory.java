//package com.gerrywen.seckill.redis.factory;
//
//import com.gerrywen.seckill.redis.config.RedisConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * program: spring-boot-seckill->RedisPoolFactory
// * description: 工厂类
// * author: gerry
// * created: 2020-03-03 21:51
// **/
//@Service
//public class RedisPoolFactory {
//
//    final RedisConfig redisConfig;
//
//    public RedisPoolFactory(RedisConfig redisConfig) {
//        this.redisConfig = redisConfig;
//    }
//
//    @Bean
//    public JedisPool JedisPoolFactory() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
//        poolConfig.setMinIdle(redisConfig.getMinIdle());//设置最小空闲数
//        poolConfig.setMaxWaitMillis(redisConfig.getMaxWait());
//        poolConfig.setMaxTotal(200); // 最大空连接数
//        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
//        poolConfig.setTestOnBorrow(true);
//        // #连接超时时是否阻塞，false时报异常,ture阻塞直到超时, 默认true
//        poolConfig.setTestOnReturn(true);
//
//
//        //Idle时进行连接扫描
//        poolConfig.setTestWhileIdle(true);
//        //表示idle object evitor两次扫描之间要sleep的毫秒数
//        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
//        //表示idle object evitor每次扫描的最多的对象数
//        poolConfig.setNumTestsPerEvictionRun(10);
//        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
//        poolConfig.setMinEvictableIdleTimeMillis(60000);
//
//        return new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
//                redisConfig.getTimeOut() * 1000, redisConfig.getPassword(), redisConfig.getDatabase());
//    }
//}
