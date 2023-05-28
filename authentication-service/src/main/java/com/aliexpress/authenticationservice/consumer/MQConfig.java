package com.aliexpress.authenticationservice.consumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    private static final String EXCHANGE_NAME = "authentication";

    @Bean
    public TopicExchange userAuthenticationServiceExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userAuthenticationServiceQueue() {
        return new Queue("user-authentication-service-queue");
    }

    @Bean
    public Binding userAuthenticationServiceBinding(Queue userAuthenticationServiceQueue, TopicExchange userAuthenticationServiceExchange) {
        return BindingBuilder.bind(userAuthenticationServiceQueue).to(userAuthenticationServiceExchange).with("authentication.#");
    }
}
