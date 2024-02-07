package com.ecommerce.Service;
import com.ecommerce.Entity.Permission;
import com.ecommerce.Request.AuthRequest;
import com.ecommerce.Request.RegisterRequest;
import com.ecommerce.Entity.CustomUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService{
    CustomUser getCurrentUser();
    CustomUser saveUser(CustomUser user);
    CustomUser getUser(String username);
    List<CustomUser> getUsers();
    CustomUser registerUser(RegisterRequest registerRequest);
    CustomUser loginUser(AuthRequest authRequest);
    Set<String> getPermissionsByUsername(String username);
    UserDetailsService getUserDetailService();
}