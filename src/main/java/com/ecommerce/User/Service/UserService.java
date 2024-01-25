package com.ecommerce.User.Service;
import com.ecommerce.User.Entity.AuthRequest;
import com.ecommerce.User.Entity.RegisterRequest;
import com.ecommerce.User.Entity.Roles;
import com.ecommerce.User.Entity.CustomUser;

import java.util.List;

public interface UserService {
    CustomUser saveUser(CustomUser user);
    Roles saveRoles(Roles roles);
    void addRoleToUser(String username, String roleName);
    CustomUser getUser(String username);
    List<CustomUser> getUsers();
    CustomUser registerUser(RegisterRequest registerRequest);
    CustomUser loginUser(AuthRequest authRequest);
}