package com.aliexpress.authenticationservice.commands;

import com.aliexpress.authenticationservice.models.Message;
import com.aliexpress.authenticationservice.publisher.AuthenticationServiceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticateCommand implements Command{


    @Autowired
    private AuthenticationServiceProducer authenticationServiceProducer;


    @Override
    public void execute(Message message) {
        System.out.println("Authentication command executed");
        Message responseMessage = new Message(
                CommandEnum.AuthenticateCommand,
                new HashMap<>(),
                "authentication-service"
        );


    }
}
