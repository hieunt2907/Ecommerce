package com.shino.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final long jwtExpirationMs = 86400000; // 1 day
    private final KeyPair keyPair;

    public JwtUtil() {
        // Generate RSA key pair for RS256
        this.keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    }

    private PrivateKey getSigningKey() {
        return keyPair.getPrivate();
    }

    private PublicKey getVerificationKey() {
        return keyPair.getPublic();
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getVerificationKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getVerificationKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return ((List<?>) claims.get("roles")).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getVerificationKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        String token = request.getParameter("token");
        if (token != null) {
            return token;
        }
        return null;
    }

    public long getExpirationDurationInSeconds(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        long nowMillis = System.currentTimeMillis();
        return (expiration.getTime() - nowMillis) / 1000;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getVerificationKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}