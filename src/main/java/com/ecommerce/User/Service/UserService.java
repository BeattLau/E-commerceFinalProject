package com.ecommerce.User.Service;
import com.ecommerce.User.Controller.LoginRequest;
import com.ecommerce.User.Controller.RegisterRequest;
import com.ecommerce.User.Model.Roles;
import com.ecommerce.User.Model.CustomUser;

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