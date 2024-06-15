package backend.invitation;

import backend.team.Team;
import backend.team.TeamService;
import backend.user.User;
import backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;
    private final TeamService teamService;

    @Autowired
    public InvitationService(InvitationRepository invitationRepository, UserService userService, TeamService teamService) {
        this.invitationRepository = invitationRepository;
        this.userService = userService;
        this.teamService = teamService;
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

    @Transactional
    public Invitation respondToInvitation(Long id, String response) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invitation ID"));

        if (!"accept".equalsIgnoreCase(response) && !"decline".equalsIgnoreCase(response)) {
            throw new IllegalArgumentException("Invalid response");
        }

        if ("accept".equalsIgnoreCase(response)) {
            invitation.setStatus("accepted");

            User fromUser = invitation.getFrom();
            User toUser = invitation.getTo();
            Team team = teamService.findByUser(fromUser);
            if (team == null) {
                team = new Team();
                team.setName(fromUser.getUsername() + " & " + toUser.getUsername() + "'s Team");
                team.getMembers().add(fromUser);
                team.getMembers().add(toUser);
                teamService.saveTeam(team);
            } else {
                teamService.addMember(team, toUser);
            }
        } else {
            invitation.setStatus("declined");
        }

        return invitationRepository.save(invitation);
    }
}
