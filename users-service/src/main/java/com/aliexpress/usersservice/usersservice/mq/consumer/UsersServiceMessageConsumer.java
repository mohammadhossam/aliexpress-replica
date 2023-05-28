package com.aliexpress.usersservice.usersservice.mq.consumer;

import com.aliexpress.usersservice.usersservice.models.Command.CommandInterface;
import com.aliexpress.usersservice.usersservice.models.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceMessageConsumer {
    @RabbitListener(queues = "user-authentication-service-queue")
    public void processMessage(Message message) {
        String commandClassName = message.getCommand().getName();

        try {
            Class<?> commandClass = Class.forName(commandClassName);
            CommandInterface commandInstance = (CommandInterface) commandClass.getDeclaredConstructor().newInstance();
            commandInstance.execute(message);
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }

    }
}
