package com.zsgs.bankapi.config.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtilPak {
    
    private static final String SECRET_KEY_STRING = "NK56tmfM47d8XlsXnoYDmz3UPMWtur4T";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    
    public String generateTolen(UserDetails userDetails) {
        return Jwts.builder()
               .subject(userDetails.getUsername())
               .claim("rolrs", userDetails.getAuthorities())
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))// 1 he
               .signWith(SECRET_KEY, Jwts.SIG.HS256)
               .compact();
    }
    
    
    public String generateTolenUser(UserDetails userDetails,String accountNumber) {
        return Jwts.builder()
               .subject(userDetails.getUsername())
               .claim("rolrs", userDetails.getAuthorities())
               .claim("accNo", accountNumber)
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))// 1 he
               .signWith(SECRET_KEY, Jwts.SIG.HS256)
               .compact();
    }
   
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claimsResolver.apply(claims);
    }
}