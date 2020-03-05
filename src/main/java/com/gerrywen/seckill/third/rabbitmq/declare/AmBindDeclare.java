package com.gerrywen.seckill.third.rabbitmq.declare;

import com.alibaba.fastjson.JSONObject;
import com.gerrywen.seckill.third.rabbitmq.bean.ModeExchange;
import com.gerrywen.seckill.third.rabbitmq.exception.MqException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description: 绑定
 *
 * @author wenguoli
 * @date 2020/3/5 9:00
 */
@Component
public class AmBindDeclare extends AbstractDeclare {
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public AmBindDeclare(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    /**
     * 队列与交换机进行绑定
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     * @return
     */
    public boolean queueBind(String queueName, String exchangeName, String routingKey) {
        return this.bind(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);
    }

    /**
     * 绑定队列
     * 该绑定的默认交换机是 amq.direct 交换机, direct类型的交换机且开启持久化
     * 路由键也是该队列
     *
     * @param queueName
     * @return
     */
    public boolean queueBind(String queueName) {
        return this.queueBind(queueName, ModeExchange.DEFAULT_DIRECT_EXCHANGE, queueName);
    }

    /**
     * 交换机和交换机进行绑定
     *
     * @param destExchangeName 目标交换机名称
     * @param exchangeName     交换机名称
     * @param routingKey       路由键
     * @return
     */
    public boolean exchangeBind(String destExchangeName, String exchangeName, String routingKey) {
        return this.bind(destExchangeName, Binding.DestinationType.EXCHANGE, exchangeName, routingKey, null);
    }

    /**
     * bind绑定
     *
     * @param destName     目标名称（可以是队列 也可以是交换机）
     * @param type         绑定的类型 交换机 / 队列
     * @param exchangeName 交换机的名称
     * @param routingKey   路由键
     * @param map          结构参数
     * @return
     */
    public boolean bind(String destName, Binding.DestinationType type, String exchangeName, String routingKey, Map<String, Object> map) {
        this.logger.info("bind parameter is destName: " + destName + ", type: " + type.name()
                + ", exchangeName: " + exchangeName + ", routingKey: " + routingKey + ", map: " + JSONObject.toJSONString(map));

        Binding binding = new Binding(destName, Binding.DestinationType.QUEUE, exchangeName, routingKey, map);
        try {
            this.rabbitAdmin.declareBinding(binding);
        } catch (Exception e) {
            if (this.logger.isDebugEnabled()) {
                throw new MqException(e.getMessage());
            }
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public void definedValidate(Object object) {

    }
}
