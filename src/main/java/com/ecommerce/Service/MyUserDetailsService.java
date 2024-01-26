package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Roles;
import com.ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;
    @Autowired
    public MyUserDetailsService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Roles::getRoleName).toArray(String[]::new))
                .build();
    }
    public String addUser(CustomUser customUser) {
        customUser.setPassword(encoder.encode(customUser.getPassword()));
        userRepository.save(customUser);
        return "User Added Successfully";
    }
}