package com.io.wallet_service.infrastructure.security;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.io.wallet_service.domain.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private final SecretKey key = Keys.hmacShaKeyFor(
        "super-secret-key-min-32-chars-long".getBytes()
    );

    private final long EXPIRATION_MS = 24 * 60 * 60 * 1000;


    public String generateToken(User user){
        return Jwts.builder()
        .subject(user.getId().toString())
        .claim("email", user.getEmail())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
        .signWith(key)
        .compact();
    }

    public UUID getUserId(String token){
        return UUID.fromString(
            Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject()
        );
    }
}
