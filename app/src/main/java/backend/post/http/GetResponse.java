package backend.post.http;

import java.util.List;

import lombok.Data;

@Data
public class GetResponse {
    private Long id;
    private String title;
    private String content;
    private String username;
    private List<GetImageResponse> images;
}
