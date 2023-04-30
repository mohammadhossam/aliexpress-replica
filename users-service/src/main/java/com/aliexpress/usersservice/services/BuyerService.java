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
//        System.out.println(registrationRequest.getFirstName());
//        System.out.println(registrationRequest.getLastName());
//        System.out.println(registrationRequest.getEmail());
//        System.out.println(registrationRequest.getPhone());
//        System.out.println(registrationRequest.getBirthdate());
//        System.out.println(registrationRequest.getAddress());
//        System.out.println(registrationRequest.getHashedPassword());

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
