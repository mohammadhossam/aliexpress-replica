package com.aliexpress.authenticationservice.consumer;

import com.aliexpress.authenticationservice.commands.Command;
import com.aliexpress.authenticationservice.models.Message;
import com.aliexpress.authenticationservice.services.BuyerAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceConsumer.class);

    @Autowired
    private final BuyerAuthenticationService buyerAuthenticationService;

    public AuthenticationServiceConsumer(BuyerAuthenticationService buyerAuthenticationService) {
        this.buyerAuthenticationService = buyerAuthenticationService;
    }

    @RabbitListener(queues = "user-authentication-service-queue")
    public void processMessage(Message message) {
        LOGGER.info("Message received from:" + message.getCommand().getExchange());
        String commandName = message.getCommand().getName();
        try {
            Class<?> commandClass = Class.forName(commandName);
            Command commandInstance = (Command) commandClass.getDeclaredConstructor().newInstance();
            commandInstance.execute(message);
            LOGGER.info("Received message as specific class: {}", message);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while processing message: {} ", message, e);
        }
    }
}
