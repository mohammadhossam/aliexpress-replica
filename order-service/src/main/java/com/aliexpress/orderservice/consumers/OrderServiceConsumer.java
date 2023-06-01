package com.aliexpress.orderservice.consumers;


import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderServiceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceConsumer.class);

    private final OrderService orderService;

    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToOrd.name}"})
    public void consumeMessage(Message message) {
        String commandClassName = message.getCommand().getName();

        try {
            Class<?> commandClass = Class.forName(commandClassName);
            logger.info(commandClass.getName());
            Command commandInstance = (Command) commandClass.getDeclaredConstructor(OrderService.class).newInstance(orderService);
            logger.info(commandInstance.getClass().getName());
            commandInstance.execute(message);
        }
        catch (Exception e) {
            logger.info("Error executing command: " + e.getMessage());
        }
    }
}
