package com.gerrywen.seckill.third.redis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * program: spring-boot-seckill->RedisConfig
 * description: redis配置
 * author: gerry
 * created: 2020-03-03 21:41
 **/
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfig {
    //   主机地址
    @Value("${spring.redis.host}")
    public String host;
    //端口
    @Value("${spring.redis.port}")
    public int port;
    //密码没有不填写
    @Value("${spring.redis.password}")
    public String password;
    // Redis数据库索引（默认为0）
    @Value("${spring.redis.database}")
    public int database;
    //连接超时时间（毫秒）
    @Value("${spring.redis.timeout}")
    public Integer timeOut;

    //最大活跃连接
    @Value("${spring.redis.lettuce.pool.max-active}")
    public int maxActive;
    //连接池最大阻塞等待时间（使用负值表示没有限制）
    @Value("${spring.redis.lettuce.pool.max-wait}")
    public Integer maxWait;
    //连接池中的最大空闲连接
    @Value("${spring.redis.lettuce.pool.max-idle}")
    public int maxIdle;
    //连接池中的最小空闲连接
    @Value("${spring.redis.lettuce.pool.min-idle}")
    public int minIdle;


    @Value("${spring.redis.cluster.nodes}")
    public String nodes;
    @Value("${spring.redis.cluster.maxRedirects}")
    public String maxRedirects;


}
