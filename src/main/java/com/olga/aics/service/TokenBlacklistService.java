package com.olga.aics.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    // 使用 ConcurrentHashMap 來存儲黑名單 token，確保線程安全
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    
    // 用於定期清理過期 token 的排程器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TokenBlacklistService() {
        // 每小時清理一次過期的 token
        scheduler.scheduleAtFixedRate(this::cleanExpiredTokens, 1, 1, TimeUnit.HOURS);
    }

    // 將 token 加入黑名單
    public void blacklistToken(String token, long expirationTime) {
        blacklistedTokens.put(token, expirationTime);
    }

    // 檢查 token 是否在黑名單中
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    // 清理過期的 token
    private void cleanExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }

    // 在應用關閉時清理資源
    public void shutdown() {
        scheduler.shutdown();
    }
} 