package com.ecommerce.User.Service;

import com.ecommerce.Auth.AuthenticationResponse;
import com.ecommerce.Jwt.JwtUtil;
import com.ecommerce.User.Entity.AuthRequest;
import com.ecommerce.User.Entity.RegisterRequest;
import com.ecommerce.User.Entity.Roles;
import com.ecommerce.User.Repository.UserRepository;
import com.ecommerce.User.Entity.CustomUser;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Builder
    public AuthenticationResponse register(RegisterRequest request) {
        CustomUser user = CustomUser.builder()
                .id(1L)
                .username("Test.1")
                .name("Test name")
                .username("user@test.com")
                .password("password123")
                .roles(Set.of(new Roles("USER")))
                .build();

        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername());
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}