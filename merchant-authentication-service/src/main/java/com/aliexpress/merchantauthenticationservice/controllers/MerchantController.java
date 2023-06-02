package com.aliexpress.merchantauthenticationservice.controllers;

import com.aliexpress.merchantauthenticationservice.dto.MerchantAuthenticationRequest;
import com.aliexpress.merchantauthenticationservice.dto.MerchantAuthenticationResponse;
import com.aliexpress.merchantauthenticationservice.dto.MerchantLogoutResponse;
import com.aliexpress.merchantauthenticationservice.dto.MerchantRegistrationRequest;
import com.aliexpress.merchantauthenticationservice.services.MerchantAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantAuthenticationService merchantAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<MerchantAuthenticationResponse> registerMerchant(@RequestBody MerchantRegistrationRequest registrationRequest) {

        System.out.println(registrationRequest);
        return ResponseEntity.ok(merchantAuthenticationService.registerMerchant(registrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<MerchantAuthenticationResponse> authenticateMerchant(@RequestBody MerchantAuthenticationRequest merchantAuthenticationRequest) {
        return ResponseEntity.ok(merchantAuthenticationService.authenticateMerchant(merchantAuthenticationRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<MerchantLogoutResponse> logoutMerchant(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(merchantAuthenticationService.logoutMerchant(authorizationHeader));
    }

}
