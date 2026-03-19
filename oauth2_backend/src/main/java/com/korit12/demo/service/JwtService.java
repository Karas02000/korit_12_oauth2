package com.korit12.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtService {
    private final SecretKey secretKey;
    private final Long expiration;

    public JwtService(@Value("${jwt.secret}") String secret,  @Value("${jwt.expiration}") Long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // JWT 생성
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role",role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // Token 상에서 email을 추출
    public String extractEmail(String token) {
        return parseClims(token).getSubject();
    }

    // Token 상에서 role을 추출
    public String extractRole(String token) {
        return parseClims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT : {}",e.getMessage());
        } catch (JwtException e) {
            log.warn("유효하지 않은 JWT : {}",e.getMessage());
        }
        return false;
    }

    private Claims parseClims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
