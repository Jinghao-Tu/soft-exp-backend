package backend.user.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String tripPreference;

    // getter和setter方法
}
