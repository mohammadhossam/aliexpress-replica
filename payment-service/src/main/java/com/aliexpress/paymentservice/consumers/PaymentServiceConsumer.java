package com.aliexpress.paymentservice.consumers;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentServiceConsumer{
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceConsumer.class);

    private final PaymentService paymentService;
    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToPay.name}"})
    public void consumeMessage(Message message) {
        String commandClassName = message.getCommand().getName();

        try {
            Class<?> commandClass = Class.forName(commandClassName);
            logger.info(commandClass.getName());
            Command commandInstance = (Command) commandClass.getDeclaredConstructor(PaymentService.class).newInstance(paymentService);
            logger.info(commandInstance.getClass().getName());
            commandInstance.execute(message);
        }
        catch (Exception e) {
            logger.info("Error executing command: " + e.getMessage());
        }
    }
}
