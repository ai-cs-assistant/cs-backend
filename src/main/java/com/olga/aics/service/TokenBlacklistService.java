package com.olga.aics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 將 token 加入黑名單
    public void blacklistToken(String token, long expirationTime) {
        String key = BLACKLIST_PREFIX + token;
        // 計算剩餘的有效時間（毫秒）
        long ttl = expirationTime - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(key, "1", ttl, TimeUnit.MILLISECONDS);
        }
    }

    // 檢查 token 是否在黑名單中
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
} 