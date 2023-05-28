package com.aliexpress.authenticationservice.commands;

import com.aliexpress.authenticationservice.models.Message;
import com.aliexpress.authenticationservice.publisher.AuthenticationServiceProducer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class AuthenticateCommand implements Command{


    private final AuthenticationServiceProducer authenticationServiceProducer;

    @Override
    public void execute(Message message) {
        System.out.println("Authentication command executed");
        Message responseMessage = new Message(
                CommandEnum.AuthenticateCommand,
                new HashMap<>(),
                "authentication-service",
                "authentication",
                "authentication.#"
        );
        authenticationServiceProducer.send(responseMessage, message.getExchange(), message.getRoutingKey());
    }
}
