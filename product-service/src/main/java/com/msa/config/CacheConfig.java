package com.msa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfig {
  @Value("${authentication.cache.host}")
  private String authenticationCacheHost;

  @Value("${authentication.cache.port}")
  private int authenticationCachePort;

  @Bean()
  public RedisTemplate<String, String> authenticationCache() {
    RedisTemplate<String, String> authenticaionCacheTemplate = new RedisTemplate<>();
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(authenticationCacheHost,
        authenticationCachePort);
    // create a connection factory, use other connection factory if you want
    LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
    factory.afterPropertiesSet();
    authenticaionCacheTemplate.setConnectionFactory(factory);
    authenticaionCacheTemplate.setKeySerializer(new StringRedisSerializer());
    authenticaionCacheTemplate.setValueSerializer(new StringRedisSerializer());
    authenticaionCacheTemplate.afterPropertiesSet();
    return authenticaionCacheTemplate;
  }

}