package com.aliexpress.inventoryservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQInvToPayConfig {
    @Value("${rabbitmq.jsonQueueInvToPay.name}")
    private String jsonQueueNameInvToPay;
    @Value("${rabbitmq.exchangeInvToPay.name}")
    private String exchangeNameInvToPay;
    @Value("${rabbitmq.jsonBindingInvToPay.routingKey}")
    private String jsonRoutingKeyInvToPay;

    //Spring bean for rabbitmq queue

    @Bean
    public Queue jsonQueueInvToPay() {
        return new Queue(jsonQueueNameInvToPay);
    }

    //Spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchangeInvToPay() {
        return new TopicExchange(exchangeNameInvToPay);
    }

    //Binding between and exchange using routing key
    @Bean
    public Binding jsonBindingInvToPay() {
        return BindingBuilder.bind(jsonQueueInvToPay()).
                to(exchangeInvToPay()).
                with(jsonRoutingKeyInvToPay);
    }

}
