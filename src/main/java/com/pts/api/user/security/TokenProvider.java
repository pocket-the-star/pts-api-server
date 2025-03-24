package com.pts.api.user.security;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private String secretKey;
    private String algorithm;

    public TokenProvider(@Qualifier("jwtSecretKey") String secretKey,
        @Qualifier("jwtAlgorithm") String algorithm) {
        this.secretKey = secretKey;
        this.algorithm = algorithm;
    }

    public String create(Long userId, UserRole userRole, TokenType tokenType, Long expireTime) {
        Map<String, Object> headerMap = generateHeaderMap();

        Map<String, Object> claims = generateClaims(userId, userRole, tokenType);

        Date expireDateTime = generateExpireDateTime(expireTime);

        return Jwts.builder().setHeader(headerMap).setClaims(claims).setExpiration(expireDateTime)
            .signWith(secretKeyHash(), io.jsonwebtoken.SignatureAlgorithm.HS256).compact();
    }

    private Key secretKeyHash() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Map<String, Object> generateMap() {

        return new HashMap<>();
    }

    private Map<String, Object> generateClaims(Long userId, UserRole userRole,
        TokenType tokenType) {
        Map<String, Object> claims = generateMap();
        claims.put("userId", userId);
        claims.put("type", tokenType.getValue());
        claims.put("role", userRole.getRole());
        return claims;
    }

    private Date generateExpireDateTime(Long expireTime) {
        Date expireDateTime = new Date();
        expireDateTime.setTime(expireDateTime.getTime() + expireTime);
        return expireDateTime;
    }

    private Map<String, Object> generateHeaderMap() {
        Map<String, Object> headerMap = generateMap();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", algorithm);
        return headerMap;
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKeyHash())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다");
        }
    }

    public Long getUserId(String token) {
        Claims claims = parseToken(token);

        return claims.get("userId", Long.class);
    }

    public UserRole getUserRole(String token) {
        Claims claims = parseToken(token);

        return UserRole.valueOf(claims.get("role", String.class));
    }

    public TokenType getTokenType(String token) {
        Claims claims = parseToken(token);

        return TokenType.valueOf(claims.get("type", Integer.class));
    }
}
