package com.ecommerce.Security;

import com.ecommerce.Config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String secret;
    private int expiration;

    public JwtUtil(JwtConfig jwtConfig){
        this.secret=jwtConfig.getSecret();
        this.expiration=jwtConfig.getExpiration();
    }

    public String generateToken(UserDetails userDetails) {
        String token =Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();

        System.out.println("Generated JWT: " + token);
        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
