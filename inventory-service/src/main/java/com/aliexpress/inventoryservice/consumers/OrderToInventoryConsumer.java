package com.aliexpress.inventoryservice.consumers;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderToInventoryConsumer {

    private final InventoryService inventoryService;
    @RabbitListener(queues = {"${rabbitmq.jsonQueueOrdToInv.name}"})
    public void consumeMessage(Message message) {
        String commandClassName = message.getCommand().getName();
        try {
            Class<?> commandClass = Class.forName(commandClassName);
            log.info(commandClass.getName());
            Command commandInstance = (Command) commandClass.getDeclaredConstructor(InventoryService.class).newInstance(inventoryService);
            commandInstance.execute(message);
        }
        catch (Exception e) {
            log.info(String.format("Error executing %s command: %s", commandClassName,e.getMessage()));
        }
    }
}
