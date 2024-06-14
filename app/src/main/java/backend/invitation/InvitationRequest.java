package backend.invitation;

import lombok.Data;

@Data
public class InvitationRequest {
    private String from;
    private String to;
}
