package User.Controller;
import User.Dto.UserLoginDto;
import User.Model.RoleToUserForm;
import User.Model.Roles;
import User.Model.User;
import User.Dto.UserRegistrationDto;
import User.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userServiceImpl.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userServiceImpl.saveUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedUser);
    }
    @PostMapping("/user/role")
    public ResponseEntity<Roles> saveRole(@RequestBody Roles roles) {
        Roles savedRole = userServiceImpl.saveRoles(roles);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRole.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedRole);
    }
    @PostMapping("/user/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userServiceImpl.addRoleToUser(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        userServiceImpl.registerUser(registrationDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto userLoginDto){
        userServiceImpl.loginUser(userLoginDto);
        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
    }
}