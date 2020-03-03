package com.gerrywen.seckill.third.redis;
import com.gerrywen.seckill.MainApplication;
import org.junit.Test;

import com.gerrywen.seckill.third.redis.domain.User;
import com.gerrywen.seckill.third.redis.key.UserKey;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * program: spring-boot-seckill->Test
 * description:
 * author: gerry
 * created: 2020-03-03 22:14
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class RedisTest {

    @Autowired
    RedisService redisService;

    @Test
    public void getUser() {
        User user = userInfo();
        redisService.get(UserKey.getById, "" + user.getId(), user.getClass());
    }

    @Test
    public void setUser() {
        User user = userInfo();
        boolean set = redisService.set(UserKey.getById, "" + user.getId(), user);
        System.out.println(set);
    }

    public User userInfo() {
        User user = new User();
        user.setId(1);
        user.setName("测试用户");
        return user;
    }
}
