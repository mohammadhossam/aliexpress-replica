package com.aliexpress.usersservice.usersservice.controllers;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import com.aliexpress.usersservice.usersservice.models.Message;
import com.aliexpress.usersservice.usersservice.repositories.BuyerRepository;
import com.aliexpress.usersservice.usersservice.services.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PutMapping("/update/{buyerId}")
    public ResponseEntity<String> updateBuyer(@PathVariable Integer buyerId, @RequestBody Buyer buyer, @RequestHeader(value = "Authorization") String authHeader) {
        return buyerService.updateBuyer(buyerId, buyer, authHeader);
    }

//    @PutMapping("/update/{buyerId}")
//    public CompletableFuture<ResponseEntity<String>> updateBuyer(@PathVariable Integer buyerId, @RequestBody Buyer buyer) {
//
//        CompletableFuture<Boolean> authResponseFuture = CompletableFuture.supplyAsync(() ->
//                buyerService.isAuthenticated(buyerId, buyer)
//        );
//
//        return authResponseFuture.thenApply(authResponse -> {
//            if (authResponse != null && authResponse) {
//                return buyerService.updateBuyer(buyerId, buyer);
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Is user authenticated? " + authResponse);
//            }
//        });
//    }

//    @GetMapping("/testMQ")
//    public void testMQ(@RequestBody Message message) {
//        buyerService.testMQ(message);
//    }
}
