package com.aliexpress.usersservice.services;

import com.aliexpress.usersservice.models.BuyerRegistrationRequest;
import com.aliexpress.usersservice.repositories.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buyer")
public class BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerBuyer(@RequestBody BuyerRegistrationRequest registrationRequest) {
        Boolean success = buyerRepository.registerBuyer(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                registrationRequest.getPhone(),
                registrationRequest.getBirthdate(),
                registrationRequest.getAddress(),
                registrationRequest.getHashedPassword());

        if (success) {
            return ResponseEntity.ok("Buyer registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register buyer");
        }
    }

}
