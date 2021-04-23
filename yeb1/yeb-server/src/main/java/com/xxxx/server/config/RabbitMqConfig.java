package com.xxxx.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.MailContext;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RabbitMqConfig {
    private static final Logger LOGGER= LoggerFactory.getLogger(RabbitMqConfig.class);
    @Resource
    private CachingConnectionFactory cachingConnectionFactory;
    @Resource
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if (ack){
                LOGGER.info("=============消息投递成功",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",1).eq("msgId",msgId));
            }else {
                LOGGER.error("============消息投递失败",msgId);
            }
        });
        /**
         * 消息失败回调，比如route不到queue时回调
         * msg：消息主题
         * repCode：响应码
         * repText：响应描述
         * exchange：交换机
         * routingKey：路由键
         */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            LOGGER.error("=================消息发送queue时失败",msg.getBody());
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue queue(){
        return new Queue(MailContext.MAIL_QUEUE_NAME);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailContext.MAIL_EXCHANGE_NAME);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailContext.MAIL_ROUTING_KEY_NAME);
    }
}
