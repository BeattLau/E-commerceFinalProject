package com.ecommerce.Service;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import com.ecommerce.Entity.Roles;
import com.ecommerce.Entity.CustomUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    CustomUser saveUser(CustomUser user);
    Roles saveRoles(Roles roles);
    void addRoleToUser(String username, String roleName);
    CustomUser getUser(String username);
    List<CustomUser> getUsers();
    CustomUser registerUser(RegisterRequest registerRequest);
    CustomUser loginUser(AuthRequest authRequest);

    UserDetailsService MyUserDetailService();
}