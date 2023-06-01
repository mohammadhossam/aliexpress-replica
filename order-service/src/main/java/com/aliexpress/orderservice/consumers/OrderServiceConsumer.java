package com.aliexpress.orderservice.consumers;


import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderServiceConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToOrd.name}"})
    public void consumeMessage(Message message) {
        String commandClassName = message.getCommand().getName();

        try {
            Class<?> commandClass = Class.forName(commandClassName);
            log.info(commandClass.getName());
            Command commandInstance = (Command) commandClass.getDeclaredConstructor(OrderService.class).newInstance(orderService);
            log.info(commandInstance.getClass().getName());
            commandInstance.execute(message);
        }
        catch (Exception e) {
            log.info(String.format("Error executing %s command: %s", commandClassName,e.getMessage()));
        }
    }
}
