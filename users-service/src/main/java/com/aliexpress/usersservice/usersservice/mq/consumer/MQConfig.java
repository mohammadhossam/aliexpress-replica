package com.aliexpress.usersservice.usersservice.mq.consumer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    private static final String EXCHANGE_NAME = "user";

    @Bean
    public TopicExchange userServiceExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userServiceQueue() {
        return new Queue("user-service-queue");
    }

    @Bean
    public Binding userServiceBinding(Queue userServiceQueue, TopicExchange userServiceExchange) {
        return BindingBuilder.bind(userServiceQueue).to(userServiceExchange).with("user.#");
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return  rabbitTemplate;
    }
}