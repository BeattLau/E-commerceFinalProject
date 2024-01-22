package User.Service;
import User.Controller.LoginRequest;
import User.Controller.RegisterRequest;
import User.Model.Roles;
import User.Model.CustomUser;
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
    public CustomUser saveUser(CustomUser user) {
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
        CustomUser user = userRepository.findByEmail(email);
        Roles role = rolesRepository.findByName(roleName);
        user.getRoles().add(role);
    }
    @Override
    public CustomUser getUser(String email) {
        log.info("Fetching user {}", email);
        return userRepository.findByEmail(email);
    }
    @Override
    public List<CustomUser> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
    @Override
    public CustomUser registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user {}", registerRequest.getEmail());
        CustomUser user = new CustomUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Roles userRole = rolesRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return user;
    }

    @Override
    public CustomUser loginUser(LoginRequest loginRequest) {
        log.info("Logging in user {}", loginRequest.getEmail());
        CustomUser user = userRepository.findByEmail(loginRequest.getEmail());
        if (user != null) {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.info("User logged in: " + user.getEmail());
                return user;
            } else {
                log.warn("Invalid password for user: " + user.getEmail());
            }
        } else {
            log.warn("User not found: " + loginRequest.getEmail());
        }
        return null;
    }
}
