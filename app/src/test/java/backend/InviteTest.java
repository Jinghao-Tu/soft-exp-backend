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

import backend.invitation.Invitation;
import backend.invitation.InvitationRepository;
import backend.user.User;
import backend.user.UserRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InviteTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void InviteTest() {
        // create two user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setHobby("Reading");
        User user2 = new User();
        user2.setUsername("test2");
        user2.setPassword("123456");
        user2.setHobby("Reading");
        // save user    
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        }
        if (userRepository.findByUsername(user2.getUsername()) == null) {
            userRepository.save(user2);
        }
        // create an invitation
        Invitation invitation = new Invitation();

        invitation.setFrom(user);
        invitation.setTo(user2);
        invitation.setStatus("pending");
        // save invitation
        invitationRepository.save(invitation);
        // check if invitation is saved
        Assertions.assertNotNull(invitationRepository.findByToUsername(user2.getUsername()));

        // check if invitation is accepted
        invitation.setStatus("accepted");
        invitationRepository.save(invitation);
        Assertions.assertEquals("accepted", invitationRepository.findByToUsername(user2.getUsername()).get(0).getStatus());

        // check if invitation is declined
        invitation.setStatus("declined");
        invitationRepository.save(invitation);
        Assertions.assertEquals("declined", invitationRepository.findByToUsername(user2.getUsername()).get(0).getStatus());

        // delete invitation
        invitationRepository.delete(invitation);
        // check if invitation is deleted
        Assertions.assertEquals(invitationRepository.findByToUsername(user2.getUsername()).size(), 0);

        // delete user
        userRepository.delete(user);
        userRepository.delete(user2);
        // check if user is deleted
        Assertions.assertNull(userRepository.findByUsername(user.getUsername()));
        Assertions.assertNull(userRepository.findByUsername(user2.getUsername()));
    }
    
}
