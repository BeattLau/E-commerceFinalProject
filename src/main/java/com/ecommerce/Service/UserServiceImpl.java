package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Permission;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    public CustomUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
            return (CustomUser) authentication.getPrincipal();
        } else {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }
    }

    @Override
    public CustomUser saveUser(CustomUser user) {
        log.info("Saving new user {} to the database", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public CustomUser getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<CustomUser> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public CustomUser registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user {}", registerRequest.getUsername());
        CustomUser user = new CustomUser();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Set<String> userPermissions = determineUserPermissions(registerRequest.getRole());
        user.setPermissions(userPermissions);

        userRepository.save(user);
        return user;
    }

    private Set<String> determineUserPermissions(String role) {
        switch (role) {
            case "ADMIN":
                return Permission.getRolePermissions(Permission.Role.ADMIN).iterator().next().getPermissions();
            case "SELLER":
                return Permission.getRolePermissions(Permission.Role.SELLER).iterator().next().getPermissions();
            case "CUSTOMER":
            default:
                return Permission.getRolePermissions(Permission.Role.CUSTOMER).iterator().next().getPermissions();
        }
    }

    @Override
    public CustomUser loginUser(AuthRequest authRequest) {
        log.info("Logging in user {}", authRequest.getUsername());
        CustomUser user = userRepository.findByUsername(authRequest.getUsername());
        if (user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
    @Override
    public Set<String> getPermissionsByUsername(String username) {
        CustomUser user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getPermissions();
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public UserDetailsService getUserDetailService() {
        return myUserDetailsService;
    }

}
