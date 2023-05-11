package com.aliexpress.usersservice.usersservice.services;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import com.aliexpress.usersservice.usersservice.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class BuyerService {
    private final BuyerRepository repository;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> updateBuyer(Integer buyerId, Buyer buyer) {
        boolean success = repository.updateBuyer(
                buyerId,
                buyer.getFirstName(),
                buyer.getLastName(),
                buyer.getEmail(),
                buyer.getPhoneNumber(),
                buyer.getBirthdate(),
                buyer.getAddress(),
                buyer.getPassword() == null ? null : passwordEncoder.encode(buyer.getPassword()));
        if (!success) throw new RuntimeException("Failed to update buyer");
        return ResponseEntity.ok("Buyer updated successfully");
    }
}
