package backend.invitation;

import backend.user.User;
import backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;

    @Autowired
    public InvitationService(InvitationRepository invitationRepository, UserService userService) {
        this.invitationRepository = invitationRepository;
        this.userService = userService;
    }

    public Invitation sendInvitation(String fromUsername, String toUsername) {
        User fromUser = userService.getUserByUsername(fromUsername);
        User toUser = userService.getUserByUsername(toUsername);
        if (fromUser == null || toUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        Invitation invitation = new Invitation();
        invitation.setFrom(fromUser);
        invitation.setTo(toUser);
        invitation.setStatus("pending");

        return invitationRepository.save(invitation);
    }

    public List<Invitation> getInvitations(String username) {
        return invitationRepository.findByToUsername(username);
    }

    public Invitation respondToInvitation(Long id, String response) {
        Invitation invitation = invitationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
        if (!response.equals("accepted") && !response.equals("declined")) {
            throw new IllegalArgumentException("Invalid response");
        }
        invitation.setStatus(response);
        return invitationRepository.save(invitation);
    }
}
