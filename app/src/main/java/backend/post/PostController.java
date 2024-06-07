package backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import backend.user.User;
import backend.user.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> addPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("username") String username,
                                        @RequestParam("images") List<MultipartFile> images) {
        logger.info("Adding post");
        try {
            List<PostImage> postImages = new ArrayList<>();
            for (MultipartFile image : images) {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

                PostImage postImage = new PostImage();
                postImage.setUrl(base64Image);
                postImages.add(postImage);
            }
            
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setImages(postImages);

            User user = userService.getUserByUsername(username);
            post.setUser(user);

            postService.savePost(post);

            return ResponseEntity.ok(post);
        } catch (IOException e) {
            logger.error("Error adding post", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        logger.info("Getting all posts");
        return ResponseEntity.ok(postService.getAllPosts());
    }

}
