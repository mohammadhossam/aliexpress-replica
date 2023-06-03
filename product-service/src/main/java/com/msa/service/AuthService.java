package com.msa.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
  private final RedisTemplate<String, String> authenticationCache;

  public boolean checkToken(String token, String merchantId) {
    String tokenParts[] = token.split(" ");
    String cacheKey = "merchant_token:" + merchantId;
    String cachedToken = (String) authenticationCache.opsForValue().get(cacheKey);
    return cachedToken != null && cachedToken.equals(tokenParts[1]);
  }
}
