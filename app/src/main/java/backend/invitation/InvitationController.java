package backend.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvitationController {

    private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);
    private final InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/invite")
    public ResponseEntity<Invitation> sendInvitation(@RequestBody InvitationRequest request) {
        logger.info("Received invitation request from: {} to: {}", request.getFrom(), request.getTo());
        Invitation invitation = invitationService.sendInvitation(request.getFrom(), request.getTo());
        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<Invitation>> getInvitations(@RequestParam String username) {
        logger.info("Fetching invitations for user: {}", username);
        List<Invitation> invitations = invitationService.getInvitations(username);
        return ResponseEntity.ok(invitations);
    }

    @PostMapping("/invite/{id}/respond")
    public ResponseEntity<Invitation> respondToInvitation(@PathVariable Long id, @RequestParam String response) {
        logger.info("Received response to invitation. ID: {}, Response: {}", id, response);
        Invitation invitation = invitationService.respondToInvitation(id, response);
        return ResponseEntity.ok(invitation);
    }
}
