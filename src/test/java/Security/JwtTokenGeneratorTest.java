package Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtTokenGeneratorTest {

    @Test
    void generateToken_ShouldGenerateValidToken() {
        // Given
        String username = "testuser";
        String secret = "secret-key";
        long expiration = 86400000L;

        String token = JwtTokenGenerator.generateToken(username, secret, expiration);
        assertNotNull(token);
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        assertEquals(username, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
