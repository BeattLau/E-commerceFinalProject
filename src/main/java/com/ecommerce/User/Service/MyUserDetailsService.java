package com.ecommerce.User.Service;

import com.ecommerce.User.Entity.CustomUser;
import com.ecommerce.User.Entity.Roles;
import com.ecommerce.User.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Roles::getName).toArray(String[]::new))
                .build();
    }

    public String addUser(CustomUser customUser) {
        customUser.setPassword(encoder.encode(customUser.getPassword()));
        userRepository.save(customUser);
        return "User Added Successfully";
    }
}