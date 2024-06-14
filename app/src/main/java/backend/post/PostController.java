package backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.post.http.GetImageResponse;
import backend.post.http.GetResponse;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
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
            Post post = new Post();

            List<PostImage> postImages = new ArrayList<>();
            for (MultipartFile image : images) {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

                PostImage postImage = new PostImage();
                postImage.setUrl(base64Image);
                postImage.setPost(post);
                postImages.add(postImage);
            }

            post.setTitle(title);
            post.setContent(content);
            post.setImages(postImages);

            User user = userService.getUserByUsername(username);
            if (user == null) {
                logger.error("User not found");
                return ResponseEntity.notFound().build();
            }
            post.setUser(user);

            postService.savePost(post);

            return ResponseEntity.ok(post);
        } catch (IOException e) {
            logger.error("Error adding post", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/posts")
    public ResponseEntity<List<GetResponse>> getAllPosts() {
        logger.info("Getting all posts");
        List<Post> posts = postService.getAllPosts();
        try {
            logger.info("All posts: " + objectMapper.writeValueAsString(posts));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        List<GetResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            GetResponse response = new GetResponse();
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setContent(post.getContent());
            response.setUsername(post.getUser().getUsername());
            List<GetImageResponse> images = new ArrayList<>();
            for (PostImage postImage : post.getImages()) {
                GetImageResponse image = new GetImageResponse();
                image.setId(postImage.getId());
                image.setImageUrl(postImage.getUrl());
                images.add(image);
            }
            response.setImages(images);
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/posts")
    public ResponseEntity<?> deleteAllPosts() {
        logger.info("Deleting all posts");
        postService.deleteAllPosts();
        return ResponseEntity.ok().build();
    }

}
