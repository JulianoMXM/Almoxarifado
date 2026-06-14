package com.example.Almoxarifado.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {
    @Value("${jwt.secret:your-secret-key-change-in-production}")
    private String secret;

    @Value("${jwt.expiration:3600}")
    private Long expirationSeconds;

    public String generateToken(String email, Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(email)
                .withClaim("userId", userId)
                .withClaim("email", email)
                .withExpiresAt(Instant.now().plus(expirationSeconds, ChronoUnit.SECONDS))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public String validateAndGetEmail(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public Long validateAndGetUserId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asLong();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}    