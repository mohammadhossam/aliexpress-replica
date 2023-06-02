package com.aliexpress.usersservice.usersservice.controllers;

import com.aliexpress.usersservice.usersservice.models.Merchant;
import com.aliexpress.usersservice.usersservice.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PutMapping("/update/{merchantId}")
    public ResponseEntity<String> updateMerchant(@PathVariable Integer merchantId, @RequestBody Merchant merchant, @RequestHeader(value = "Authorization") String authHeader) {
        return merchantService.updateMerchant(merchantId, merchant, authHeader);
    }
}
