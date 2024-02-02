package com.ecommerce.Service;
import com.ecommerce.Entity.CustomUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUserName(String token);
    String generateToken(CustomUser customUser, Map<String, Object> extraClaims, Long jwtExpiration);
    boolean isTokenValid(String token, UserDetails userDetails);
    Long getJwtExpiration();
}