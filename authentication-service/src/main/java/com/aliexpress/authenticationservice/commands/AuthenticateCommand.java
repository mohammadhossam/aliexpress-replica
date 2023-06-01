package com.aliexpress.authenticationservice.commands;

import com.aliexpress.authenticationservice.models.Message;
import com.aliexpress.authenticationservice.publisher.AuthenticationServiceProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Component("AuthenticateCommand")
public class AuthenticateCommand implements Command{


    private final AuthenticationServiceProducer authenticationServiceProducer;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void execute(Message message) {
        System.out.println("Checking Token Validity..");
        Map<String, Object> data = message.getDataMap();
        String token = (String) data.get("token");
        String email = (String) data.get("email");
        String tokenKey = "buyer_token:" + email;
        String tokenFromRedis = redisTemplate.opsForValue().get(tokenKey);
        Message responseMessage = new Message(
                CommandEnum.AuthenticateCommand,
                new HashMap<>(),
                "authentication-service",
                "authentication",
                "authentication.#"
        );
        if (tokenFromRedis == null || !tokenFromRedis.equals(token)) {
            System.out.println("Token is not valid");
            responseMessage.getDataMap().put("isAuthenticated", false);
        }
        else {
            System.out.println("Token is valid");
            responseMessage.getDataMap().put("isAuthenticated", true);
        }

        authenticationServiceProducer.send(responseMessage, message.getExchange(), message.getRoutingKey());
    }
}
