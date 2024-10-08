package com.carhut.securityservice.security.util;

import com.carhut.securityservice.security.model.RawUser;
import com.carhut.securityservice.repository.AuthorityRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Autowired
    private AuthorityRepository authorityRepository;
    private SecretKey key = Jwts.SIG.HS256.key().build();

    @Autowired
    public JwtUtil(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public String generateToken(RawUser rawUser, Map<String, Object> claims) {
        return createToken(rawUser, claims);
    }

    private String createToken(RawUser rawUser, Map<String, Object> claims) {

        Date dateCreated = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24));

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(rawUser.getUsername())
                .claim("authorities", authorityRepository.getAuthorityById(rawUser.getAuthorityId()))
                .setIssuedAt(dateCreated)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();

        return token;
    }

    public boolean isTokenValid(String token, String username) {
        if (username == null || token == null) {
            return false;
        }
        return extractUsername(token).equals(username) && !isTokenExpired(token);
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
        } catch (Exception e) {
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

}
