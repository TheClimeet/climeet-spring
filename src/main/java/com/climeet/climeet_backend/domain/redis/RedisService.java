package com.climeet.climeet_backend.domain.redis;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithExpiration(String key, String value){
        Duration timeout = Duration.ofDays(10);
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
