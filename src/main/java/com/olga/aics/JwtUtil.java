package com.olga.aics;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.secret-key}")
    private String secretKey;

    // Token 有效時間
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 2; // 測試用: 2分鐘失效
    //private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15;//有效 15 分鐘

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 建立 Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Token 主體：使用者名稱
                .setIssuedAt(new Date()) // 簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY)) // 過期時間
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 使用密鑰簽章
                .compact();
    }

    // 驗證 Token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token); // 驗證簽章 + 過期時間
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 從 Token 取得使用者名稱
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 從 Token 取得過期時間
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 萃取任意 Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
