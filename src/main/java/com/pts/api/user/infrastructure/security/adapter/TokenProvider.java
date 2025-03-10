package com.pts.api.user.infrastructure.security.adapter;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.port.out.TokenProviderPort;
import com.pts.api.user.common.exception.InvalidTokenException;
import com.pts.api.user.domain.model.TokenPayload;
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
public class TokenProvider implements TokenProviderPort {

    private String secretKey;
    private String algorithm;

    public TokenProvider(@Qualifier("jwtSecretKey") String secretKey,
        @Qualifier("jwtAlgorithm") String algorithm) {
        this.secretKey = secretKey;
        this.algorithm = algorithm;
    }

    @Override
    public String create(TokenPayload payload, Long expireTime) {
        Map<String, Object> headerMap = generateHeaderMap();

        Map<String, Object> claims = generateClaims(payload);

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

    private Map<String, Object> generateClaims(TokenPayload payload) {
        Map<String, Object> claims = generateMap();
        claims.put("userId", payload.getUserId());
        claims.put("type", payload.getTokenTypeValue());
        claims.put("role", payload.getUserRoleValue());
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

    @Override
    public TokenPayload parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyHash())
                .build()
                .parseClaimsJws(token)
                .getBody();

            return TokenPayload.builder()
                .userId(Long.parseLong(claims.get("userId").toString()))
                .tokenType(TokenType.valueOf(Integer.parseInt(claims.get("type").toString())))
                .userRole(UserRole.valueOf(claims.get("role").toString()))
                .build();
        } catch (Exception e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다");
        }
    }

}
