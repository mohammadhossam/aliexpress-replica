package com.aliexpress.orderservice.config;

import com.aliexpress.orderservice.models.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, List<Order>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<Order>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        return template;
    }
}