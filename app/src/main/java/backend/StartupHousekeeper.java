package backend;

import backend.comments.CommentService;
import backend.invitation.InvitationRepository;
import backend.team.TeamRepository;
import backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupHousekeeper {

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;
    @EventListener(ApplicationReadyEvent.class)
    public void clearInvitations() {
        invitationRepository.deleteAll();
        teamRepository.deleteAll();
        userService.deleteAllUsers();
        commentService.deleteAllComments();
        System.out.println("All invitations have been cleared.");
        System.out.println("All teams have been cleared.");
        System.out.println("All users have been cleared.");
        System.out.println("All comments have been cleared.");
    }
}
