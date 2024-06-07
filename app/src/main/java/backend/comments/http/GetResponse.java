package backend.comments.http;

import lombok.Data;

@Data
public class GetResponse {
    private Long id;
    private Long postId;
    private String content;
    private String username;
    private String avatar;
}
