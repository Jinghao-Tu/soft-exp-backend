package backend.comments.http;

import lombok.Data;

@Data
public class AddRequest {
    private Long postId;
    private String avatar;
    private String username;
    private String content;
}
