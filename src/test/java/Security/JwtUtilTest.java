package Security;
import com.ecommerce.eCommerceFinalProjectApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = eCommerceFinalProjectApplication.class)
public class JwtUtilTest {
/*
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
*/
}

