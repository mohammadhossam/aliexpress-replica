package com.aliexpress.usersservice.usersservice.controllers;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import com.aliexpress.usersservice.usersservice.repositories.BuyerRepository;
import com.aliexpress.usersservice.usersservice.services.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PutMapping("/update/{buyerId}")
    public ResponseEntity<String> updateBuyer(@PathVariable Integer buyerId, @RequestBody Buyer buyer) {
        return buyerService.updateBuyer(buyerId, buyer);
    }
}
