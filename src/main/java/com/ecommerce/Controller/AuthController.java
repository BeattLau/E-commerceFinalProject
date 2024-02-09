package com.ecommerce.Controller;
import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import com.ecommerce.Service.JwtServiceImpl;
import com.ecommerce.Service.UserServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;

    @PostMapping("/register")
    public ResponseEntity<CustomUser> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomUser user) {
        Map<String, Object> extraClaims = new HashMap<>();
        String token = jwtService.generateToken(user, extraClaims);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CustomUser>> adminData() {
        List<CustomUser> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}
