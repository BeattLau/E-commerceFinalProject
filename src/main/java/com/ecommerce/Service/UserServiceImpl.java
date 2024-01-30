package com.ecommerce.Service;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import com.ecommerce.Entity.Roles;
import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Repository.RolesRepository;
import com.ecommerce.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomUser saveUser(CustomUser user) {
        log.info("Saving new user {} to the database", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Roles saveRoles(Roles roles) {
        log.info("Saving new role {} to the database", roles.getRoleName());
        return rolesRepository.save(roles);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user{}", roleName, username);
        CustomUser user = userRepository.findByUsername(username);
        Roles role = rolesRepository.findByRoleName(roleName);
        user.getRoles().add(role);
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
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Roles userRole = rolesRepository.findByRoleName("ROLE_ADMIN");
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return user;
    }

    @Override
    public CustomUser loginUser(AuthRequest authRequest) {
        log.info("Logging in user {}", authRequest.getUsername());
        CustomUser user = userRepository.findByUsername(authRequest.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                log.info("User logged in: " + user.getUsername());
                return user;
            } else {
                log.warn("Invalid password for user: " + user.getUsername());
            }
        } else {
            log.warn("User not found: " + authRequest.getUsername());
        }
        return null;
    }

    @Override
    public UserDetailsService MyUserDetailService() {
        return this::getUser;
    }

    @Override
    public Set<Roles> getRolesByUsername(String username) {
        Optional<CustomUser> customUserOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (customUserOptional.isPresent()) {
            CustomUser customUser = customUserOptional.get();
            return customUser.getRoles();
        } else {
            return Collections.emptySet();
        }
    }
}