package Security;
import eCommerce.eCommerceFinalProjectApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
@SpringBootTest(classes = eCommerceFinalProjectApplication.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secret;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtUtil.setExpiration(86400L);
        jwtUtil.setSecret("secret-key");
    }

    @Test
    public void testGenerateToken() {
        User userDetails = new User("testuser", "testpassword", Collections.emptyList());

        String token = jwtUtil.generateToken(userDetails.getUsername());
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Assertions.assertEquals("testuser", claims.getSubject());
    }

    @Test
    public void testValidateToken() {
        String validToken = JwtTokenGenerator.generateToken("testuser", "secret-key", 86400000L);

        UserDetails userDetails = new User("testuser", "testpassword", Collections.emptyList());
        boolean isValid = jwtUtil.validateToken(validToken, userDetails);
        Assertions.assertTrue(isValid);
    }

}

