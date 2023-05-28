package com.aliexpress.authenticationservice.publisher;

import com.aliexpress.authenticationservice.models.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AuthenticationServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Message message, String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
