package com.ecommerce.User.Controller;
import com.ecommerce.User.Model.CustomUser;
import com.ecommerce.User.Service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<CustomUser> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }
    @PostMapping("login")
    public ResponseEntity<CustomUser> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }


}
