package com.aliexpress.authenticationservice.controllers;

import com.aliexpress.authenticationservice.consumer.AuthenticationServiceConsumer;
import com.aliexpress.authenticationservice.dto.*;
import com.aliexpress.authenticationservice.models.Buyer;
import com.aliexpress.authenticationservice.publisher.AuthenticationServiceProducer;
import com.aliexpress.authenticationservice.repositories.BuyerRepository;
import com.aliexpress.authenticationservice.services.BuyerAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/authentication/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerAuthenticationService buyerAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<BuyerAuthenticationResponse> registerBuyer(@RequestBody BuyerRegistrationRequest registrationRequest) {

        return ResponseEntity.ok(buyerAuthenticationService.registerBuyer(registrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<BuyerAuthenticationResponse> authenticateBuyer(@RequestBody BuyerAuthenticationRequest buyerAuthenticationRequest) {
        return ResponseEntity.ok(buyerAuthenticationService.authenticateBuyer(buyerAuthenticationRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<BuyerLogoutResponse> logoutBuyer(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(buyerAuthenticationService.logoutBuyer(authorizationHeader));
    }

}
