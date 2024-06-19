package backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(String oldUsername, User newUser) {
        User user = userRepository.findByUsername(oldUsername);
        if (user == null) {
            return null;
        }
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());
        user.setHobby(newUser.getHobby());
        user.setAvatar(newUser.getAvatar());
        return userRepository.save(user);
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public void deleteAllUsers() {
    }
}

