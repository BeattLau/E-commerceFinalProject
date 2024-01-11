package User.Service;
import User.Dto.UserRegistrationDto;
import User.Model.User;

public interface UserService {
    User registerUser(UserRegistrationDto registrationDto);
}