package backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backend.user.request.UpdateRequest;
import jakarta.annotation.PostConstruct;
import backend.user.request.RegisterRequest;
import backend.user.request.LoginRequest;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        logger.info("UserController started");
        // add default users
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setHobby("Reading");
        if (userService.getUserByUsername(admin.getUsername()) == null) {
            userService.saveUser(admin);
        }
        User user1 = new User();
        user1.setUsername("usr1");
        user1.setPassword("123456");
        user1.setHobby("Playing");
        if (userService.getUserByUsername(user1.getUsername()) == null) {
            userService.saveUser(user1);
        }
        User user2 = new User();
        user2.setUsername("usr2");
        user2.setPassword("123456");
        user2.setHobby("Looking");
        if (userService.getUserByUsername(user2.getUsername()) == null) {
            userService.saveUser(user2);
        }
    }

    @GetMapping("allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Getting all users");
        return ResponseEntity.ok(userService.getAllUsers().stream().toList());
    }

    @GetMapping("user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("Getting user by username: " + username);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            logger.error("User not found: " + username);
            return ResponseEntity.notFound().build();
        }
        logger.info("User found: {}", user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/update")
    public ResponseEntity<User> updateUser(@RequestBody UpdateRequest updateRequest) {
        logger.info("Updating user: " + updateRequest.getUsername());
        String oldUsername = updateRequest.getUsername();
        User newUser = new User();
        newUser.setUsername(updateRequest.getNewNickname());
        newUser.setPassword(updateRequest.getNewPassword());
        newUser.setHobby(updateRequest.getHobby());
        User updatedUser = userService.updateUser(oldUsername, newUser);
        if (updatedUser == null) {
            logger.error("User not found: " + oldUsername);
            return ResponseEntity.notFound().build();
        }
        logger.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Registering user: " + registerRequest.getUsername());
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            logger.error("Passwords do not match");
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        if (userService.getUserByUsername(registerRequest.getUsername()) != null) {
            logger.error("Username already exists: " + registerRequest.getUsername());
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setHobby("");
        userService.saveUser(user);
        logger.info("User registered: {}", user);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("avatar", "default-avatar.png");
        // userInfo.put("hobby", user.getHobby());
        userInfo.put("hobby", "");
        logger.info("User info: {}", userInfo);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Logging in user: " + loginRequest.getUsername());
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            logger.error("User not found: " + loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Error user or password");
        }
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            logger.error("Incorrect password");
            return ResponseEntity.badRequest().body("Error user or password");
        }
        logger.info("User logged in: {}", user);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("avatar", "default-avatar.png");
        // userInfo.put("hobby", user.getHobby());
        userInfo.put("hobby", "");
        logger.info("User info: {}", userInfo);
        return ResponseEntity.ok(userInfo);
    }

}