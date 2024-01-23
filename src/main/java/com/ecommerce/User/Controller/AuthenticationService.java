package com.ecommerce.User.Controller;

import com.ecommerce.Security.JwtUtil;
import com.ecommerce.User.Model.Roles;
import com.ecommerce.User.Repository.UserRepository;
import com.ecommerce.User.Model.CustomUser;
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
                .email("user@test.com")
                .password("password123")
                .roles(Set.of(new Roles("USER")))
                .build();

        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}