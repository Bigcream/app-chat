package com.example.appchat.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key, Object object){
        redisTemplate.opsForValue().set(key, object);
    }

    public Object findByKey(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public List<Object> findAllByKeyPattern(String keyPattern){
        Set<String> keys = redisTemplate.keys(keyPattern);
        assert keys != null;
        return redisTemplate.opsForValue().multiGet(keys);
    }
}
