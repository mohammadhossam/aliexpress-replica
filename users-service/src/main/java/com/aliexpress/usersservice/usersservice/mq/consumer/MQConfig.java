package com.aliexpress.usersservice.usersservice.mq.consumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
}