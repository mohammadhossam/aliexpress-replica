package com.aliexpress.merchantauthenticationservice.services;

import com.aliexpress.merchantauthenticationservice.configs.JwtService;
import com.aliexpress.merchantauthenticationservice.dto.MerchantAuthenticationRequest;
import com.aliexpress.merchantauthenticationservice.dto.MerchantAuthenticationResponse;
import com.aliexpress.merchantauthenticationservice.dto.MerchantLogoutResponse;
import com.aliexpress.merchantauthenticationservice.dto.MerchantRegistrationRequest;
import com.aliexpress.merchantauthenticationservice.models.Merchant;
import com.aliexpress.merchantauthenticationservice.models.Role;
import com.aliexpress.merchantauthenticationservice.repositories.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantAuthenticationService {

    private final MerchantRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;


    public MerchantAuthenticationResponse registerMerchant(MerchantRegistrationRequest registrationRequest) {
        var merchant = Merchant.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .taxNumber(registrationRequest.getTaxNumber())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhone())
                .address(registrationRequest.getAddress())
                .birthdate(registrationRequest.getBirthdate())
                .role(Role.MERCHANT)
                .build();
        System.out.println(merchant);
        boolean success = repository.registerMerchant(
                merchant.getFirstName(),
                merchant.getLastName(),
                merchant.getTaxNumber(),
                merchant.getEmail(),
                merchant.getPhoneNumber(),
                merchant.getBirthdate(),
                merchant.getAddress(),
                merchant.getPassword(),
                merchant.getRole().name());
        if (!success) throw new RuntimeException("Failed to register merchant");
        merchant = repository.findMerchantByEmail(merchant.getEmail()).orElseThrow();
        String id = String.valueOf(merchant.getId());
        String tokenKey = "merchant_token:" + id;
        var jwtToken = jwtService.generateToken(merchant);
        redisTemplate.opsForValue().set(tokenKey, jwtToken);

        return MerchantAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public MerchantAuthenticationResponse authenticateMerchant(MerchantAuthenticationRequest merchantAuthenticationRequest) {

        var merchant = repository.findMerchantByEmail(merchantAuthenticationRequest.getEmail())
                .orElseThrow();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        merchant.getId(),
                        merchantAuthenticationRequest.getPassword()
                )
        );



        String id = String.valueOf(merchant.getId());
        String tokenKey = "merchant_token:" + id;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(tokenKey))) {
            String jwtToken = redisTemplate.opsForValue().get(tokenKey);
            return MerchantAuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }

        var jwtToken = jwtService.generateToken(merchant);
        redisTemplate.opsForValue().set(tokenKey, jwtToken);

        return MerchantAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public MerchantLogoutResponse logoutMerchant(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String id = jwtService.extractUserId(token);
        String tokenKey = "merchant_token:" + id;
        boolean success = Boolean.TRUE.equals(redisTemplate.delete(tokenKey));
        return MerchantLogoutResponse.builder()
                .message(success ? "Logout success" : "User doesn't exist or not logged in")
                .build();
    }
}
