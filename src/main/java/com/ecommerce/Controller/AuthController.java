package com.ecommerce.Controller;
import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import com.ecommerce.Service.UserServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<CustomUser> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<CustomUser> loginUser(@RequestBody AuthRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CustomUser>> adminData() {
        List<CustomUser> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}
