//package backend;
//
//import backend.invitation.InvitationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class StartupHousekeeper {
//
//    @Autowired
//    private InvitationRepository invitationRepository;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void clearInvitations() {
//        invitationRepository.deleteAll();
//        System.out.println("All invitations have been cleared.");
//    }
//}
