package backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.user.http.LoginRequest;
import backend.user.http.RegisterRequest;
import backend.user.http.UpdateRequest;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        } else {
            // update password
            User newUser = userService.getUserByUsername(admin.getUsername());
            newUser.setPassword("123456");
            userService.updateUser(admin.getUsername(), newUser);
        }
        User user1 = new User();
        user1.setUsername("usr1");
        user1.setPassword("123456");
        user1.setHobby("Playing");
        if (userService.getUserByUsername(user1.getUsername()) == null) {
            userService.saveUser(user1);
        } else {
            // update password
            User newUser = userService.getUserByUsername(user1.getUsername());
            newUser.setPassword("123456");
            userService.updateUser(user1.getUsername(), newUser);
        }
        User user2 = new User();
        user2.setUsername("usr2");
        user2.setPassword("123456");
        user2.setHobby("Looking");
        if (userService.getUserByUsername(user2.getUsername()) == null) {
            userService.saveUser(user2);
        } else {
            // update password
            User newUser = userService.getUserByUsername(user2.getUsername());
            newUser.setPassword("123456");
            userService.updateUser(user2.getUsername(), newUser);
        }
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Getting all users");
        return ResponseEntity.ok(userService.getAllUsers().stream().toList());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("Getting user by username: " + username);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            logger.error("User not found: " + username);
            return ResponseEntity.notFound().build();
        }
        try {
            logger.info("User found: {}", objectMapper.writeValueAsString(user));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        // logger.info("User found: {}", user);
        return ResponseEntity.ok(user);
    }

    // @PostMapping("/users/update")
    // public ResponseEntity<?> check (@RequestBody Object request) {
    //     // logger.info("Checking request: " + request);
    //     try {
    //         logger.info("Checking request: {}", objectMapper.writeValueAsString(request));
    //     } catch (Exception e) {
    //         logger.error("Error: " + e.getMessage());
    //     }
    //     return ResponseEntity.ok().build();
    // }

    @PostMapping("/users/update")
    public ResponseEntity<User> updateUser(@RequestBody UpdateRequest request) {
        // logger.info("Updating user: " + request.getUsername());
        try {
            logger.info("Updating user: {}", objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        String oldUsername = request.getUsername();
        User newUser = userService.getUserByUsername(oldUsername);
        if (newUser == null) {
            logger.error("User not found: " + oldUsername);
            return ResponseEntity.notFound().build();
        }
        if (request.getNewNickname() != null && !request.getNewNickname().equals(newUser.getUsername())) {
            newUser.setUsername(request.getNewNickname());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().equals(newUser.getPassword())) {
            newUser.setPassword(request.getNewPassword());
        }
        if (request.getHobby() != null && !request.getHobby().equals(newUser.getHobby())) {
            newUser.setHobby(request.getHobby());
        }
        User updatedUser = userService.updateUser(oldUsername, newUser);
        if (updatedUser == null) {
            logger.error("Error updating user: " + oldUsername);
            return ResponseEntity.badRequest().build();
        }
        try {
            logger.info("User updated: {}", objectMapper.writeValueAsString(updatedUser));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        // logger.info("User updated: {}", updatedUser);
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
        // logger.info("User registered: {}", user);
        try {
            logger.info("User registered: {}", objectMapper.writeValueAsString(user));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("avatar", "default-avatar.png");
        // userInfo.put("hobby", user.getHobby());
        userInfo.put("hobby", "");
        // logger.info("User info: {}", userInfo);
        try {
            logger.info("User info: {}", objectMapper.writeValueAsString(userInfo));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
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
        // logger.info("User logged in: {}", user);
        try {
            logger.info("User logged in: {}", objectMapper.writeValueAsString(user));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("avatar", "default-avatar.png");
        // userInfo.put("hobby", user.getHobby());
        userInfo.put("hobby", "");
        logger.info("User info: {}", userInfo);
        return ResponseEntity.ok(userInfo);
    }

}