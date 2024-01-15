package Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenGenerator {

    public static String generateToken(String username, String secret, long expiration) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static void main(String[] args) {
        String username = "testuser";
        String secret = "your-secret-key";
        long expiration = 86400000L;

        String token = generateToken(username, secret, expiration);
        System.out.println("Generated Token: " + token);
    }
}
