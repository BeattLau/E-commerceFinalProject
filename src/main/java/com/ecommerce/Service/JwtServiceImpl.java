package com.ecommerce.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Getter
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {;

        System.out.println("Generating JWT for user: " + userDetails.getUsername());
        return generateToken(userDetails, extraClaims, jwtExpiration);
    }

    @Override
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims, Long jwtExpiration) {
        System.out.println("Generating JWT for user: " + userDetails.getUsername());
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .setClaims(extraClaims)
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
                .parseClaimsJws(token).getBody();
    }
}