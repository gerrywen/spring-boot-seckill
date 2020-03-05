package com.gerrywen.seckill.third.rabbitmq.config;

import com.gerrywen.seckill.third.rabbitmq.listen.InitThreadLocalPoolListen;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 * 在容器启动的时候，注册自定义的Listener
 * 在监听器中初始化线程池
 *
 * @author wenguoli
 * @date 2020/3/5 8:54
 */
@Configuration
public class ServletListenerRegistrationConfig {

    /**
     * 注册自定义的Bean
     * 并且设置监听器，该监听器初始化线程池
     *
     * 通过代码 注册Listener
     *
     * SpringBoot注册Servlet Filter Listener三大组件:
     * https://www.jianshu.com/p/6830989985e9
     */
    @SuppressWarnings("unchecked") // 编译器忽略 unchecked 警告信息
    @Bean
    public ServletListenerRegistrationBean registrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new InitThreadLocalPoolListen());
        return servletListenerRegistrationBean;
    }

}
