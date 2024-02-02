package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Getter
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(CustomUser customUser, Map<String, Object> extraClaims) {
        log.info("Generating JWT for user: {}", customUser.getUsername());
        return generateToken(customUser, extraClaims, jwtExpiration);
    }

    @Override
    public String generateToken(CustomUser customUser, Map<String, Object> extraClaims, Long jwtExpiration) {
        log.info("Generating JWT for user: {}", customUser.getUsername());
        return Jwts
                .builder()
                .setSubject(customUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .addClaims(extraClaims)
                .signWith(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
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
                .setSigningKey(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
}