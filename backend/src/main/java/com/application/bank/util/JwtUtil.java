package com.application.bank.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.application.bank.model.User;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            // SignatureException: Invalid signature
        } catch (MalformedJwtException ex) {
            // MalformedJwtException: Invalid JWT token
        } catch (ExpiredJwtException ex) {
            // ExpiredJwtException: JWT token is expired
        } catch (UnsupportedJwtException ex) {
            // UnsupportedJwtException: JWT token is unsupported
        } catch (IllegalArgumentException ex) {
            // IllegalArgumentException: JWT claims string is empty
        }
        return false;
    }
}
