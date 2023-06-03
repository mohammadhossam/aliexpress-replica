package com.aliexpress.authenticationservice.services;

import com.aliexpress.authenticationservice.configs.JwtService;
import com.aliexpress.authenticationservice.dto.*;
import com.aliexpress.authenticationservice.models.Buyer;
import com.aliexpress.authenticationservice.models.Role;
import com.aliexpress.authenticationservice.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyerAuthenticationService {

    private final BuyerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;


    public BuyerAuthenticationResponse registerBuyer(BuyerRegistrationRequest registrationRequest) {
        var buyer = Buyer.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhone())
                .address(registrationRequest.getAddress())
                .birthdate(registrationRequest.getBirthdate())
                .role(Role.BUYER)
                .build();
        boolean success = repository.registerBuyer(
                buyer.getFirstName(),
                buyer.getLastName(),
                buyer.getEmail(),
                buyer.getPhoneNumber(),
                buyer.getBirthdate(),
                buyer.getAddress(),
                buyer.getPassword(),
                buyer.getRole().name());
        if (!success) throw new RuntimeException("Failed to register buyer");
        buyer = repository.findBuyerByEmail(buyer.getEmail()).orElseThrow();
        String id = String.valueOf(buyer.getId());
        System.out.println(id);
        String tokenKey = "buyer_token:" + id;
        var jwtToken = jwtService.generateToken(buyer);
        redisTemplate.opsForValue().set(tokenKey, jwtToken);

        return BuyerAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public BuyerAuthenticationResponse authenticateBuyer(BuyerAuthenticationRequest buyerAuthenticationRequest) {

        var buyer = repository.findBuyerByEmail(buyerAuthenticationRequest.getEmail())
                .orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        buyer.getId(),
                        buyerAuthenticationRequest.getPassword()
                )
        );

        String id = String.valueOf(buyer.getId());
        String tokenKey = "buyer_token:" + id;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(tokenKey))) {
            String jwtToken = redisTemplate.opsForValue().get(tokenKey);
            return BuyerAuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }

        var jwtToken = jwtService.generateToken(buyer);
        redisTemplate.opsForValue().set(tokenKey, jwtToken);

        return BuyerAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public BuyerLogoutResponse logoutBuyer(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String id = jwtService.extractUserId(token);
        String tokenKey = "buyer_token:" + id;
        boolean success = Boolean.TRUE.equals(redisTemplate.delete(tokenKey));
        return BuyerLogoutResponse.builder()
                .message(success ? "Logout success" : "User doesn't exist or not logged in")
                .build();
    }
}
