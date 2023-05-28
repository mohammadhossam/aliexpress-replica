package com.aliexpress.authenticationservice.consumer;

import com.aliexpress.authenticationservice.services.BuyerAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceConsumer.class);

    @Autowired
    private BuyerAuthenticationService buyerAuthenticationService;

    @RabbitListener(queues = "${authentication.queue.name}")
    public void listen(String message) {

        LOGGER.info("Received message as specific class: {}", message);
    }
}
