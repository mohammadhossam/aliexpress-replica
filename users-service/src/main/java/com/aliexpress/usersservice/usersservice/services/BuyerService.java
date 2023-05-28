package com.aliexpress.usersservice.usersservice.services;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import com.aliexpress.usersservice.usersservice.models.Command.CommandEnum;
import com.aliexpress.usersservice.usersservice.models.Message;
import com.aliexpress.usersservice.usersservice.mq.producer.UserAuthenticationServiceMessageProducer;
import com.aliexpress.usersservice.usersservice.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuyerService {
    private final BuyerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationServiceMessageProducer userAuthenticationServiceMessageProducer;

    public ResponseEntity<String> updateBuyer(Integer buyerId, Buyer buyer) {
        System.out.println(buyer.getPhoneNumber());
        Map<String, Object> result = repository.updateBuyer(
                buyerId,
                buyer.getFirstName(),
                buyer.getLastName(),
                buyer.getEmail(),
                buyer.getPhoneNumber(),
                buyer.getBirthdate(),
                buyer.getAddress(),
                buyer.getPassword() == null ? null : passwordEncoder.encode(buyer.getPassword()));

        boolean success = (Boolean) result.get("success");
        if (!success) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((String) result.get("reason"));
        return ResponseEntity.ok("Buyer updated successfully");
    }

    public void testMQ(String s) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("payload", s);
        Message message = new Message(CommandEnum.AuthenticateCommand, messageMap, "UsersService");
        userAuthenticationServiceMessageProducer.send(message);
    }

}