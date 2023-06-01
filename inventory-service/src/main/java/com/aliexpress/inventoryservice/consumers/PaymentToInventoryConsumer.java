package com.aliexpress.inventoryservice.consumers;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentToInventoryConsumer {
    private final ApplicationContext applicationContext;

    @RabbitListener(queues = {"${rabbitmq.jsonQueuePayToInv.name}"})
    public void consumeMessage(Message message) {
        log.info("Message received from:" + message.getExchange());
        String commandName = String.valueOf(message.getCommand());
        log.info("Command is {}", commandName);
        try {
            Command commandInstance = applicationContext.getBean(commandName, Command.class);
            commandInstance.execute(message);
            log.info("Received message as specific class: {}", message);
        } catch (Exception e) {
            log.info(String.format("Error executing %s command: %s", commandName, e.getMessage()));
        }
    }
}