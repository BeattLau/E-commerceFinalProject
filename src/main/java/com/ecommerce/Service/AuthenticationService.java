package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import com.ecommerce.Response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        CustomUser user = new CustomUser();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPermissions(Set.of("READ"));
        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.generateToken(user, extraClaims, jwtService.getJwtExpiration());
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            CustomUser user = (CustomUser) authentication.getPrincipal();

            Map<String, Object> extraClaims = new HashMap<>();
            String jwtToken = jwtService.generateToken(user, extraClaims, jwtService.getJwtExpiration());
            return new AuthenticationResponse(jwtToken);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }
}


