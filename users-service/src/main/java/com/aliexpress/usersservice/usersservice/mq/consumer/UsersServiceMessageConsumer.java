package com.aliexpress.usersservice.usersservice.mq.consumer;

import com.aliexpress.usersservice.usersservice.models.Command.CommandInterface;
import com.aliexpress.usersservice.usersservice.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersServiceMessageConsumer.class);
    @RabbitListener(queues = "user-service-queue")
    public void processMessage(Message message) {
        LOGGER.info("Message received from:" + message.toString());
//        String commandClassName = message.getCommand().getName();
//
//        try {
//            Class<?> commandClass = Class.forName(commandClassName);
//            CommandInterface commandInstance = (CommandInterface) commandClass.getDeclaredConstructor().newInstance();
//            commandInstance.execute(message);
//        } catch (Exception e) {
//            System.out.println("Error executing command: " + e.getMessage());
//        }

    }
}
