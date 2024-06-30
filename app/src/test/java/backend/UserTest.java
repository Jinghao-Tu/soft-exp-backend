package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.team.*;
import backend.invitation.InvitationRepository;
import backend.invitation.InvitationService;
import backend.user.*;
import backend.user.http.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService = new UserService(userRepository);

    @Autowired
    private TeamService teamService = new TeamService(teamRepository, userRepository);
    
    @Autowired
    private InvitationService invitationService = new InvitationService(invitationRepository, userService, teamService);

    @Autowired
    private UserController userController= new UserController(userService, invitationService);

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void UserClassTest() {
        // create a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setHobby("Reading");
        // save user
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        } else {
            userService.updateUser(user.getUsername(), user);
        }
        // get user
        User newUser = userRepository.findByUsername(user.getUsername());
        // check if user is saved
        Assertions.assertEquals(user.getUsername(), newUser.getUsername());
        Assertions.assertEquals(user.getPassword(), newUser.getPassword());
        Assertions.assertEquals(user.getHobby(), newUser.getHobby());
        // delete user
        userRepository.delete(newUser);
        // check if user is deleted
        Assertions.assertNull(userRepository.findByUsername(user.getUsername()));
    }

    @Test
    @Transactional
    public void UserServiceTest() throws Exception {
        // create a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setHobby("Reading");
        // save user
        if (userService.getUserByUsername(user.getUsername()) == null) {
            userService.saveUser(user);
        } else {
            userService.updateUser(user.getUsername(), user);
        }
        // get user
        User newUser = userService.getUserByUsername(user.getUsername());
        // check if user is saved
        Assertions.assertEquals(user.getUsername(), newUser.getUsername());
        Assertions.assertEquals(user.getPassword(), newUser.getPassword());
        Assertions.assertEquals(user.getHobby(), newUser.getHobby());
        // delete user
        userService.deleteUser(user.getUsername());
        // check if user is deleted
        Assertions.assertNull(userService.getUserByUsername(user.getUsername()));
    }

    @Test
    @Transactional
    public void UserControllerTest() throws Exception {
        // create a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");

        // save user
        if (userService.getUserByUsername(user.getUsername()) != null) {
            userService.deleteUser(user.getUsername());
        }
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(user.getUsername());
        registerRequest.setPassword(user.getPassword());
        registerRequest.setConfirmPassword(user.getPassword());
        // register user
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(registerRequest)))
            .andExpect(status().isOk());
        // get user
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + user.getUsername()))
            .andExpect(status().isOk());
        User newUser = userService.getUserByUsername(user.getUsername());
        // check if user is saved
        Assertions.assertEquals(user.getUsername(), newUser.getUsername());
        Assertions.assertEquals(user.getPassword(), newUser.getPassword());

        // login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(loginRequest)))
            .andExpect(status().isOk());

        // // update user
        // UpdateRequest updateRequest = new UpdateRequest();
        // updateRequest.setUsername(user.getUsername());
        // updateRequest.setNewNickname("test1");
        // updateRequest.setNewPassword("1234567");
        // updateRequest.setHobby("Writing");
        // // update user
        // mockMvc.perform(MockMvcRequestBuilders.post("/api/users/update")
        //     .contentType(MediaType.APPLICATION_JSON)
        //     .content(new ObjectMapper().writeValueAsString(updateRequest)))
        //     .andExpect(status().isOk());
        // // get updated user
        // User updatedUser = userService.getUserByUsername(updateRequest.getNewNickname());
        // // check if user is updated
        // Assertions.assertEquals(updateRequest.getNewNickname(), updatedUser.getUsername());
        // Assertions.assertEquals(updateRequest.getNewPassword(), updatedUser.getPassword());
        // Assertions.assertEquals(updateRequest.getHobby(), updatedUser.getHobby());

        // get all users
        mockMvc.perform(MockMvcRequestBuilders.get("/api/allusers"))
            .andExpect(status().isOk());

        // delete user
        userService.deleteUser(user.getUsername());
        // check if user is deleted
        Assertions.assertNull(userService.getUserByUsername(user.getUsername()));
   }

}
