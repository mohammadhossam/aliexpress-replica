package com.aliexpress.inventoryservice.consumers;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentToInventoryConsumer {
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceConsumer.class);

    private final InventoryService inventoryService;
    @RabbitListener(queues = {"${rabbitmq.jsonQueuePayToInv.name}"})
    public void consumeMessage(Message message) {
        logger.info("Entered pay to inv");
        String commandClassName = message.getCommand().getName();
        try {
            Class<?> commandClass = Class.forName(commandClassName);
            logger.info(commandClass.getName());
            Command commandInstance = (Command) commandClass.getDeclaredConstructor(InventoryService.class).newInstance(inventoryService);
            logger.info(commandInstance.getClass().getName());
            commandInstance.execute(message);
        }
        catch (Exception e) {
            logger.info("Error executing command: " + e.getMessage());
        }
    }
}