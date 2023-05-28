package com.aliexpress.usersservice.usersservice.mq.producer;

import com.aliexpress.usersservice.usersservice.models.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationServiceMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserAuthenticationServiceMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Message message) {
        rabbitTemplate.convertAndSend("authentication", "authentication.#", message);
    }
}