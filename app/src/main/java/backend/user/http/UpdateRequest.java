package backend.user.http;

import lombok.Data;

@Data
public class UpdateRequest {
    private String username;
    private String newNickname;
    private String newPassword;
    private String hobby;
}
