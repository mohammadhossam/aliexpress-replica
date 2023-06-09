package com.aliexpress.usersservice.usersservice.services;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import com.aliexpress.usersservice.usersservice.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuyerService {
    private final BuyerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;


    public ResponseEntity<String> updateBuyer(Integer buyerId, Buyer buyer, String authHeader) {
        if (authHeader != null) {
            String token = authHeader.substring(7);
            String tokenKey = "buyer_token:" + buyerId;
            String tokenFromRedis = redisTemplate.opsForValue().get(tokenKey);
            if (tokenFromRedis == null || !tokenFromRedis.equals(token)) {
                System.out.println("Token is not valid");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authenticated!");
            } else {
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
        } else {
            return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("No authorization header exists!");
        }
    }

}
