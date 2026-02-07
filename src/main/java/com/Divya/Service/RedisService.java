package com.Divya.Service;

import com.Divya.Response.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, String> redisTemplate,
                        ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            String value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                log.debug("Cache miss for key: {}", key);
                return null;
            }

            return objectMapper.readValue(value, clazz);

        } catch (RedisConnectionFailureException ex) {
            log.warn("Redis unavailable while reading key={}", key);
            return null;   // graceful fallback
        } catch (Exception ex) {
            log.error("Error reading cache for key={}", key, ex);
            return null;
        }
    }

    public void set(String key, Object value, long ttlMinutes) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttlMinutes, TimeUnit.MINUTES);

        } catch (RedisConnectionFailureException ex) {
            log.warn("Redis unavailable while writing key={}", key);
        } catch (Exception ex) {
            log.error("Error writing cache for key={}", key, ex);
        }
    }
}

