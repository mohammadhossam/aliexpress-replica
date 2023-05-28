package com.aliexpress.authenticationservice.consumer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
