package com.ecommerce.Controller;

import com.ecommerce.Entity.*;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Response.AuthenticationResponse;
import com.ecommerce.Response.ErrorResponse;
import com.ecommerce.Service.JwtServiceImpl;
import com.ecommerce.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private JwtServiceImpl jwtUtil;
    @Autowired
   private AuthenticationManager authenticationManager;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CustomUser>> getUsers() {
        return ResponseEntity.ok().body(userServiceImpl.getUsers());
    }

    @PostMapping("/user/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomUser> saveUser(@RequestBody CustomUser user) {
        CustomUser savedUser = userServiceImpl.saveUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getUserId())
                .toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

    @PostMapping("/user-login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                CustomUser customUser = (CustomUser) authentication.getPrincipal();
                Map<String, Object> extraClaims = new HashMap<>();

                String token = jwtUtil.generateToken(customUser, extraClaims);

                AuthenticationResponse authResponse = new AuthenticationResponse(token);
                return ResponseEntity.ok().body(authResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials"));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not found"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Authentication failed"));
        }

    }
}
