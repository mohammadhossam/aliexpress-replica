package com.aliexpress.usersservice.usersservice.controllers;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import com.aliexpress.usersservice.usersservice.models.Message;
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

//    public ResponseEntity<String> updateBuyer(@PathVariable Integer buyerId, @RequestBody Buyer buyer) {
//
//        CompletableFuture<Message> authResponseFuture = CompletableFuture.supplyAsync(() ->
//                rabbitTemplate.convertSendAndReceive("authExchange", "authQueue", authRequest)
//        );
//
//        authResponseFuture.thenAccept(authResponse -> {
//            if (authResponse != null && authResponse.isAuthenticated()) {
//                CompletableFuture<Message> validationResponseFuture = CompletableFuture.supplyAsync(() ->
//                        rabbitTemplate.convertSendAndReceive("productExchange", "productQueue", validationRequest)
//                );
//
//                validationResponseFuture.thenAccept(validationResponse -> {
//                    if (validationResponse != null && validationResponse.isValidProduct()) {
//                        payment_service.chargeNewCard(paymentRequest);
//                    }
//                });
//            }
//        });
//    }

    @GetMapping("/testMQ")
    public void testMQ(@RequestBody Message message) {
        buyerService.testMQ(message);
    }
}
