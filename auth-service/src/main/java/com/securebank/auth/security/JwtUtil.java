package com.securebank.auth.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
@Slf4j @Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private Long expiration;
    private Key key() { return Keys.hmacShaKeyFor(secret.getBytes()); }
    public String generateToken(String username, String role, Long userId) {
        Map<String,Object> c=new HashMap<>(); c.put("role",role); c.put("userId",userId);
        return Jwts.builder().setClaims(c).setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis()+expiration))
            .signWith(key(),SignatureAlgorithm.HS256).compact();
    }
    public boolean validateToken(String token) {
        try { Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token); return true; }
        catch(Exception e) { log.warn("Invalid JWT: {}",e.getMessage()); return false; }
    }
    public String extractUsername(String t) { return claims(t).getSubject(); }
    public String extractRole(String t)     { return claims(t).get("role",String.class); }
    public Long   extractUserId(String t)   { return claims(t).get("userId",Long.class); }
    private Claims claims(String t) { return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(t).getBody(); }
}
