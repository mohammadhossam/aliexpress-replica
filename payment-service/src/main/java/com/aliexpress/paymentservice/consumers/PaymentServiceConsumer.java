package com.aliexpress.paymentservice.consumers;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentServiceConsumer{
    private final PaymentService paymentService;

    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToPay.name}"})
    public void consumeMessage(Message message) {
        String commandClassName = message.getCommand().getName();

        try {
            Class<?> commandClass = Class.forName(commandClassName);
            log.info(commandClass.getName());
            Command commandInstance = (Command) commandClass.getDeclaredConstructor(PaymentService.class).newInstance(paymentService);
            log.info(commandInstance.getClass().getName());
            commandInstance.execute(message);
        }
        catch (Exception e) {
            log.info(String.format("Error executing %s command: %s", commandClassName,e.getMessage()));
        }
    }
}
