package com.ecommerce.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private SecretKey secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    public JwtServiceImpl(@Value("${token.signing.key}") String jwtSigningKey) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSigningKey.getBytes());
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(CustomUser customUser, Map<String, Object> extraClaims, Long jwtExpiration) {
        return Jwts.builder()
                .setSubject(customUser.getUsername())
                .addClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
          .compact();
    }
    public String generateToken(CustomUser customUser, Map<String, Object> extraClaims) {
        return generateToken(customUser, extraClaims, jwtExpiration);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
 Fix_auth_and_security
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

    }
}