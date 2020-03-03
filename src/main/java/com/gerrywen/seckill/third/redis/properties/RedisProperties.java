package com.gerrywen.seckill.third.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * program: spring-cloud-mall->RedisProperties
 * description:
 * author: gerry
 * created: 2019-11-26 20:46
 **/
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {
    //   主机地址
    public String host;

    //端口
    public int port;

    //密码没有不填写
    public String password;

    // Redis数据库索引（默认为0）
    public int database;

    //连接超时时间（毫秒）
    public String timeOut;


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