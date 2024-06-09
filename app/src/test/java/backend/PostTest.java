package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import backend.post.*;
import backend.user.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostTest {
            
        @Autowired
        private PostRepository postRepository;
        
        @Autowired
        private UserRepository userRepository;
    
        @Autowired
        private PostService postService = new PostService(postRepository);
        
        @Autowired
        private UserService userService = new UserService(userRepository);
    
        @Autowired
        private PostController postController = new PostController(postService, userService);
    
        @Autowired
        private MockMvc mockMvc;
    
        @Test
        @Transactional
        public void PostClassTest() {
            // create a user
            User user = new User();
            user.setUsername("test");
            user.setPassword("123456");
            user.setHobby("Reading");
            // save user
            if (userRepository.findByUsername(user.getUsername()) == null) {
                userRepository.save(user);
            } else {
                userService.updateUser(user.getUsername(), user);
            }
            // create a post
            Post post = new Post();
            post.setUser(user);
            post.setTitle("test");
            post.setContent("test");
            // save post
            postService.savePost(post);
            // get post
            List<Post> posts = postService.getAllPosts();
            // check if there is a post matching the title and content
            boolean found = false;
            for (Post p : posts) {
                if (p.getTitle().equals(post.getTitle()) && p.getContent().equals(post.getContent())) {
                    found = true;
                    break;
                }
            }
            Assertions.assertTrue(found);
        }
}
