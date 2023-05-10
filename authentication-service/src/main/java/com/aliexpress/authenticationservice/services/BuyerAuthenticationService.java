package com.aliexpress.authenticationservice.services;

import com.aliexpress.authenticationservice.configs.JwtService;
import com.aliexpress.authenticationservice.dto.*;
import com.aliexpress.authenticationservice.models.Buyer;
import com.aliexpress.authenticationservice.models.Role;
import com.aliexpress.authenticationservice.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyerAuthenticationService {

    private final BuyerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


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
        var jwtToken = jwtService.generateToken(buyer);

        String email = buyer.getEmail();
        String tokenKey = "buyer_token:" + email;
        redisTemplate.opsForValue().set(tokenKey, jwtToken);

        return BuyerAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public BuyerAuthenticationResponse authenticateBuyer(BuyerAuthenticationRequest buyerAuthenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        buyerAuthenticationRequest.getEmail(),
                        buyerAuthenticationRequest.getPassword()
                )
        );
        var buyer = repository.findBuyerByEmail(buyerAuthenticationRequest.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(buyer);

        String email = buyer.getEmail();
        String tokenKey = "buyer_token:" + email;
        redisTemplate.opsForValue().set(tokenKey, jwtToken);

        return BuyerAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public BuyerLogoutResponse logoutBuyer(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);
        String tokenKey = "buyer_token:" + email;
        boolean success = redisTemplate.delete(tokenKey);
        return BuyerLogoutResponse.builder()
                .message(success ? "Logout success" : "User doesn't exist or not logged in")
                .build();
    }
}
