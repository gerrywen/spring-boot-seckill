package com.gerrywen.seckill.third.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 *
 * RabbitMQ启动报unknown exchange type 'x-delayed-message'
 * https://blog.csdn.net/weixin_30794491/article/details/96472203
 *
 * docker环境下RabbitMQ实现延迟队列(使用delay插件,非死信队列), 注意版本要和rabbitmq一致
 * https://www.jianshu.com/p/197715cea172
 *
 * Spring Boot整合RabbitMQ详细教程
 * https://blog.csdn.net/yuyeqianhen/article/details/94594711
 *
 * RabbitMQ的三种模式-----直接模式（Direct）
 * https://blog.csdn.net/qq_22596931/article/details/89329024
 *
 * RabbitMQ的三种模式-----分列模式（Fanout）
 * https://blog.csdn.net/qq_22596931/article/details/89329039
 *
 *
 * @author wenguoli
 * @date 2020/3/4 9:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTest {

    @Autowired
    private RabbitSendServiceTest rabbitSendServiceTest;

    @Test
    public void sendTest() {
        rabbitSendServiceTest.send();
    }
}
