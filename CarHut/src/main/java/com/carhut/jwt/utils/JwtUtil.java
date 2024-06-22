package com.carhut.jwt.utils;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(userDetails, claims);
    }

    private String createToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(key)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpirationDate(token);
        return new Date(System.currentTimeMillis()).after(expirationDate);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        try {
            return claimsTFunction.apply(extractAllClaims(token));
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (UnsupportedJwtException e) {
            return null;
        }
    }

}
