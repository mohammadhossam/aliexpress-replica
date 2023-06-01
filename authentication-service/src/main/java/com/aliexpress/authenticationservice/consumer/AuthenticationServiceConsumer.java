package com.aliexpress.authenticationservice.consumer;

import com.aliexpress.authenticationservice.commands.AuthenticateCommand;
import com.aliexpress.authenticationservice.commands.Command;
import com.aliexpress.authenticationservice.models.Message;
import com.aliexpress.authenticationservice.publisher.AuthenticationServiceProducer;
import com.aliexpress.authenticationservice.services.BuyerAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceConsumer.class);

    private final BuyerAuthenticationService buyerAuthenticationService;
    private AuthenticateCommand authenticateCommand;
    private final AuthenticationServiceProducer authenticationServiceProducer;

    @Autowired
    private ApplicationContext applicationContext;

    @RabbitListener(queues = "user-authentication-service-queue")
    public void processMessage(Message message) {
        LOGGER.info("Message received from:" + message.getExchange());
        String commandName = String.valueOf(message.getCommand());
        LOGGER.info("Command is {}", commandName);
        try {
//            Class<?> commandClass = Class.forName("com.aliexpress.authenticationservice.commands." + commandName);
            Command authenticateCommand = applicationContext.getBean(commandName, Command.class);
//            authenticateCommand = new AuthenticateCommand(authenticationServiceProducer);
            authenticateCommand.execute(message);
            LOGGER.info("Received message as specific class: {}", message);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while processing message: {} ", message, e);
        }
    }
}
