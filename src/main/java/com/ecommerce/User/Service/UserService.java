package com.ecommerce.User.Service;
import com.ecommerce.Auth.LoginRequest;
import com.ecommerce.Auth.RegisterRequest;
import com.ecommerce.User.Entity.Roles;
import com.ecommerce.User.Entity.CustomUser;

import java.util.List;

public interface UserService {
    CustomUser saveUser(CustomUser user);
    Roles saveRoles(Roles roles);
    void addRoleToUser(String email, String roleName);
    CustomUser getUser(String Email);
    List<CustomUser> getUsers();
    CustomUser registerUser(RegisterRequest registerRequest);
    CustomUser loginUser(LoginRequest loginRequest);
}