package com.contactslist.api.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration}")
    private long jwtExpirationMillis;

    public String generateToken(String subject) {
        Date issuedAt = new Date();
        Date expirationDate = new Date(issuedAt.getTime() + jwtExpirationMillis);

        Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String parseSubject(String token) {
        Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
