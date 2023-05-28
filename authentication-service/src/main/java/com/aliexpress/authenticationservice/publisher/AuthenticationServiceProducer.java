package com.aliexpress.authenticationservice.publisher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceProducer.class);



    private RabbitTemplate rabbitTemplate;

    public AuthenticationServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message, String exchange, String routingKey) {
        LOGGER.info("Sending message to {}: {}", exchange ,message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
