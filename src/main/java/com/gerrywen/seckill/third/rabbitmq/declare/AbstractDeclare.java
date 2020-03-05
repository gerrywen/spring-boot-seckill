package com.gerrywen.seckill.third.rabbitmq.declare;

import com.gerrywen.seckill.third.rabbitmq.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * description:  队列  交换机  绑定等基类
 *
 * @author wenguoli
 * @date 2020/3/5 9:00
 */
public abstract class AbstractDeclare {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RabbitAdmin rabbitAdmin;

    /**
     * 自定义的校验
     *
     * @param object
     */
    public abstract void definedValidate(Object object);

    /**
     * 通用校验
     * 1. 校验字段是否是非空
     *
     * @param object
     */
    public void validate(Object object) {
        ValidateUtils.validate(object);
        this.definedValidate(object);
    }
}
