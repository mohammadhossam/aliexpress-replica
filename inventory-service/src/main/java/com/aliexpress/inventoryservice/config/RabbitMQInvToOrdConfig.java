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
public class RabbitMQInvToOrdConfig {
    @Value("${rabbitmq.jsonQueueInvToOrd.name}")
    private String jsonQueueNameInvToOrd;
    @Value("${rabbitmq.exchangeInvToOrd.name}")
    private String exchangeNameInvToOrd;
    @Value("${rabbitmq.jsonBindingInvToOrd.routingKey}")
    private String jsonRoutingKeyInvToOrd;
    @Bean
    public Queue jsonQueueInvToOrd() {
        return new Queue(jsonQueueNameInvToOrd);
    }

    //Spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchangeInvToOrd() {
        return new TopicExchange(exchangeNameInvToOrd);
    }

    //Binding between and exchange using routing key
    @Bean
    public Binding jsonBindingInvToOrd() {
        return BindingBuilder.bind(jsonQueueInvToOrd()).
                to(exchangeInvToOrd()).
                with(jsonRoutingKeyInvToOrd);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
