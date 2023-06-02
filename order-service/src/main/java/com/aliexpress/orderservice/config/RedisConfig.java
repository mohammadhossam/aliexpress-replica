package com.aliexpress.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${redis1.host}")
    private String redis1Host;

    @Value("${redis1.port}")
    private int redis1Port;

    @Value("${redis2.host}")
    private String redis2Host;

    @Value("${redis2.port}")
    private int redis2Port;

    @Bean(name = "redis1ConnectionFactory")
    public JedisConnectionFactory redis1ConnectionFactory() {
        RedisStandaloneConfiguration redis1Config = new RedisStandaloneConfiguration(redis1Host, redis1Port);
        JedisClientConfiguration jedisClientConfig = JedisClientConfiguration.builder().build();
        return new JedisConnectionFactory(redis1Config, jedisClientConfig);
    }

    @Bean(name = "redis2ConnectionFactory")
    public JedisConnectionFactory redis2ConnectionFactory() {
        RedisStandaloneConfiguration redis2Config = new RedisStandaloneConfiguration(redis2Host, redis2Port);
        JedisClientConfiguration jedisClientConfig = JedisClientConfiguration.builder().build();
        return new JedisConnectionFactory(redis2Config, jedisClientConfig);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate1(RedisConnectionFactory redis1ConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redis1ConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean(name = "redisTemplate2")
    public RedisTemplate<String, String> redisTemplate2(RedisConnectionFactory redis2ConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redis2ConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        // Set other serializers if needed
        return template;
    }

}
