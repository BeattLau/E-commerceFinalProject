package com.ecommerce.Controller;

import com.ecommerce.Entity.*;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Service.JwtServiceImpl;
import com.ecommerce.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor
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
    public ResponseEntity<List<CustomUser>> getUsers(){
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
    @PostMapping("/user/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Roles> saveRole(@RequestBody Roles roles) {
        Roles savedRole = userServiceImpl.saveRoles(roles);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRole.getRoleId())
                .toUri();
        return ResponseEntity.created(location).body(savedRole);
    }
    @PostMapping("/user/addRoleToUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userServiceImpl.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return jwtUtil.generateToken(customUserDetails);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}