package backend.user.request;

import lombok.Data;

@Data
public class UpdateRequest {
    private String username;
    private String newNickname;
    private String newPassword;
    private String hobby;
}
