package com.gerrywen.seckill.third.rabbitmq.declare;

import com.gerrywen.seckill.third.rabbitmq.bean.ModeExchange;
import com.gerrywen.seckill.third.rabbitmq.enums.ModeExchangeEnum;
import com.gerrywen.seckill.third.rabbitmq.exception.MqException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * description: 交换机
 *
 * @author wenguoli
 * @date 2020/3/5 9:01
 */
@Component
public class AmExchangeDeclare extends AbstractDeclare{
    @Autowired
    RabbitAdmin rabbitAdmin;

    /**
     * 向rabbitMQ服务器注册指定的交换机以及交换机的类型
     *
     * @param mqExchage
     * @return
     */
    public Exchange declareExchange(ModeExchange mqExchage) {
        this.logger.info("declare exchange is :" + mqExchage.toString());

        Exchange exchange = null;

        super.validate(mqExchage);
        exchange = this.initExchange(mqExchage);
        this.rabbitAdmin.declareExchange(exchange);

        this.logger.info("declare exchange success");
        return exchange;
    }


    /**
     * 从RabbitMQ服务端上删除指定的交换机
     *
     * @param exchangeName
     * @return
     */
    public boolean deleteExchange(String exchangeName) {
        this.logger.info("delete exchange is : " + exchangeName);

        if (StringUtils.isEmpty(exchangeName)) {
            throw new MqException("the parameter exchangeName couldn't not be null");
        }

        return this.rabbitAdmin.deleteExchange(exchangeName);
    }

    /**
     * 根据不同类型初始化不同类型的交换机
     *
     * @param mqExchage
     * @return
     */
    private Exchange initExchange(ModeExchange mqExchage) {
        ModeExchangeEnum exchangeTypeEnum = mqExchage.getType();
        switch (exchangeTypeEnum) {
            case DIRECT:
                return new DirectExchange(mqExchage.getName(), mqExchage.isDurable(), mqExchage.isAutoDelete(), mqExchage.getArguments());
            case TOPIC:
                return new TopicExchange(mqExchage.getName(), mqExchage.isDurable(), mqExchage.isAutoDelete(), mqExchage.getArguments());
            case FANOUT:
                return new FanoutExchange(mqExchage.getName(), mqExchage.isDurable(), mqExchage.isAutoDelete(), mqExchage.getArguments());
            case HEADERS:
                return new HeadersExchange(mqExchage.getName(), mqExchage.isDurable(), mqExchage.isAutoDelete(), mqExchage.getArguments());
            default:
                return null;
        }
    }

    /**
     * 自定义校验规则
     *
     * @param object
     */
    @Override
    public void definedValidate(Object object) {

    }
}
