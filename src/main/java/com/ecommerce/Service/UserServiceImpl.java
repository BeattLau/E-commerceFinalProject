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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomUser saveUser(CustomUser user) {
        log.info("Saving new user {} to the database", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Roles saveRoles(Roles roles) {
        log.info("Saving new role {} to the database", roles.getRole_name());
        return rolesRepository.save(roles);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user{}", roleName, username);
        CustomUser user = userRepository.findByUsername(username);
        Roles role = rolesRepository.findByRole_name(roleName);
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
        Roles userRole = rolesRepository.findByRole_name("ROLE_USER");
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
        return null;
    }
}