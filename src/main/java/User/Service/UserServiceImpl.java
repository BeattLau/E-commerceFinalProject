package User.Service;
import User.Dto.UserLoginDto;
import User.Dto.UserRegistrationDto;
import User.Model.Roles;
import User.Model.User;
import User.Repository.RolesRepository;
import User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public Roles saveRoles(Roles roles) {
        log.info("Saving new role {} to the database", roles.getName());
        return rolesRepository.save(roles);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user{}", roleName, email);
        User user = userRepository.findByEmail(email);
        Roles role = rolesRepository.findByName(roleName);
        user.getRoles().add(role);
    }
    @Override
    public User getUser(String email) {
        log.info("Fetching user {}", email);
        return userRepository.findByEmail(email);
    }
    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
    @Override
    public User registerUser(UserRegistrationDto registrationDto) {
        log.info("Registering new user {}", registrationDto.getEmail());
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Roles userRole = rolesRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return user;
    }

    @Override
    public User loginUser(UserLoginDto userLoginDto) {
        log.info("Logging in user {}", userLoginDto.getEmail());
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if (user != null) {
            if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                log.info("User logged in: " + user.getEmail());
                return user;
            } else {
                log.warn("Invalid password for user: " + user.getEmail());
            }
        } else {
            log.warn("User not found: " + userLoginDto.getEmail());
        }
        return null;
    }
}
