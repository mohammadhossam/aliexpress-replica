package com.aliexpress.usersservice.usersservice.services;

import com.aliexpress.usersservice.usersservice.models.Merchant;
import com.aliexpress.usersservice.usersservice.repositories.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<String> updateMerchant(Integer merchantId, Merchant merchant, String authHeader) {

        if (authHeader != null) {
            String token = authHeader.substring(7);
            String tokenKey = "merchant_token:" + merchantId;
            String tokenFromRedis = redisTemplate.opsForValue().get(tokenKey);
            if (tokenFromRedis == null || !tokenFromRedis.equals(token)) {
                System.out.println("Token is not valid");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authenticated!");
            } else {
                Map<String,Object> result= repository.updateMerchant(
                        merchantId,
                        merchant.getFirstName(),
                        merchant.getLastName(),
                        merchant.getEmail(),
                        merchant.getPhoneNumber(),
                        merchant.getBirthdate(),
                        merchant.getAddress(),
                        merchant.getPassword() == null ? null : passwordEncoder.encode(merchant.getPassword()),
                        merchant.getTaxNumber());

                boolean success = (Boolean) result.get("success");
                if (!success) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((String) result.get("reason"));
                return ResponseEntity.ok("Merchant updated successfully");
            }
        }else {
            return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("No authorization header exists!");
        }






    }
}
