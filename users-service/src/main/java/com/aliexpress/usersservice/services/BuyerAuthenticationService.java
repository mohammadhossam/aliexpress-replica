package com.aliexpress.usersservice.services;

import com.aliexpress.usersservice.configs.JwtService;
import com.aliexpress.usersservice.dto.BuyerAuthenticationRequest;
import com.aliexpress.usersservice.dto.BuyerAuthenticationResponse;
import com.aliexpress.usersservice.dto.BuyerRegistrationRequest;
import com.aliexpress.usersservice.models.Buyer;
import com.aliexpress.usersservice.models.Role;
import com.aliexpress.usersservice.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
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
        var jwtToken = jwtService.generateToken(buyer);
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
        return BuyerAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
