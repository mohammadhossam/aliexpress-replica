package com.aliexpress.paymentservice.consumers;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentServiceConsumer{
    private final ApplicationContext applicationContext;

    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToPay.name}"})
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
