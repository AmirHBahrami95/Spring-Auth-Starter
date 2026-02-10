package com.XX_DOMAIN_NAME.XX_APP_NAME.cache;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration 
public class CacheConfig{
	
	/*
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
	    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	    builder.modules(new JavaTimeModule());
	    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    return builder;
	}
	
	@Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5)) // Set TTL for cache
            .disableCachingNullValues()
            .serializeValuesWith(RedisSerializationContext
                .SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
    */
	
	@Bean
    public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
		return jedisConnectionFactory;
    }

	// for expired access tokens
    @Bean
    public RedisTemplate<String,Boolean> redisTemplate() {
		RedisTemplate<String,Boolean> redisTemplate=new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
    }

	@Bean
	public CachingService cachingService(){
		return new CachingService();
	}

}
