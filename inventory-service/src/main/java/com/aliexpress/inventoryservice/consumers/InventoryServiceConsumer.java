package com.aliexpress.inventoryservice.consumers;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.models.commands.DecrementInventoryCommand;
import com.aliexpress.inventoryservice.services.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@RequiredArgsConstructor
public class InventoryServiceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceConsumer.class);

    private final InventoryService inventoryService;
    @RabbitListener(queues = {"${rabbitmq.jsonQueueOrdToInv.name}"})
    public void consumeMessage(Message message) {
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
