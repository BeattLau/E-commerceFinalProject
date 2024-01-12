package User.Service;
import User.Dto.UserLoginDto;
import User.Dto.UserRegistrationDto;
import User.Model.Roles;
import User.Model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Roles saveRoles(Roles roles);
    void addRoleToUser(String email, String roleName);
    User getUser(String Email);
    List<User> getUsers();
    User registerUser(UserRegistrationDto registrationDto);
    User loginUser(UserLoginDto userLoginDto);
}