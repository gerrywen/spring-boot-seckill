package com.gerrywen.seckill.third.redis;

import com.gerrywen.seckill.MainApplication;
import com.gerrywen.seckill.third.redis.constant.RedisKey;
import com.gerrywen.seckill.third.redis.enums.CtimsModelEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * program: spring-boot-seckill->RedisTest
 * description:
 * author: gerry
 * created: 2020-03-04 07:35
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class RedisTest {

    @Autowired
    RedisService redisService;

    @Test
    public void getUser() {
        String key = RedisKey.ORDER_KEY_PREFIX_ID;
        String s = redisService.get(CtimsModelEnum.CTIMS_ORDER_CAP, key);
        System.out.println(s);
    }

    @Test
    public void setUser() {
        String key = RedisKey.ORDER_KEY_PREFIX_ID;
        redisService.set(CtimsModelEnum.CTIMS_ORDER_CAP,key, 1);
    }

}
