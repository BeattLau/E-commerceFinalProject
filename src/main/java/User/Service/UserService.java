package User.Service;
import User.Controller.LoginRequest;
import User.Controller.RegisterRequest;
import User.Model.Roles;
import User.Model.CustomUser;

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