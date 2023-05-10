package com.aliexpress.usersservice.controllers;

import com.aliexpress.usersservice.dto.*;
import com.aliexpress.usersservice.models.Buyer;
import com.aliexpress.usersservice.repositories.BuyerRepository;
import com.aliexpress.usersservice.services.BuyerAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;
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

    @GetMapping("/find/{email}")
    public ResponseEntity<Buyer> findBuyerByEmail(@PathVariable String email) {
        Optional<Buyer> buyer = buyerRepository.findBuyerByEmail(email);
        if (buyer.isPresent()) {
            return ResponseEntity.ok(buyer.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
