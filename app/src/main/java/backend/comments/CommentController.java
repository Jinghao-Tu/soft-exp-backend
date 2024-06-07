package backend.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import backend.comments.http.AddRequest;
import backend.comments.http.GetResponse;
import backend.post.Post;
import backend.post.PostService;
import backend.user.User;
import backend.user.UserService;

@RestController
@RequestMapping("/api")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService  commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody AddRequest request) {
        // logger.info("Adding comment");
        try {
            logger.info("Adding comment: " + objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        User user = userService.getUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Post post = postService.getPostById(request.getPostId());
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(request.getContent());
        comment.setAvatar(request.getAvatar());
        commentService.saveComment(comment);
        return ResponseEntity.ok(comment);
        // return null;
    }

    @GetMapping("/allcomments")
    public ResponseEntity<List<GetResponse>> getAllComments() {
        logger.info("Getting all comments");
        List<Comment> comments = commentService.getAllComments();
        try {
            logger.info("All comments: " + objectMapper.writeValueAsString(comments));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        List<GetResponse> response = comments.stream().map(comment -> {
            GetResponse getResponse = new GetResponse();
            getResponse.setId(comment.getId());
            getResponse.setUsername(comment.getUser().getUsername());
            getResponse.setPostId(comment.getPost().getId());
            getResponse.setContent(comment.getContent());
            getResponse.setAvatar(comment.getAvatar());
            return getResponse;
        }).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<GetResponse>> getCommentsByPost(@PathVariable Long postId) {
        logger.info("Getting comments by post");
        Post post = postService.getPostById(postId);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = commentService.getCommentsByPost(post);
        try {
            logger.info("Comments by post: " + objectMapper.writeValueAsString(comments));
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
        List<GetResponse> response = comments.stream().map(comment -> {
            GetResponse getResponse = new GetResponse();
            getResponse.setId(comment.getId());
            getResponse.setUsername(comment.getUser().getUsername());
            getResponse.setPostId(comment.getPost().getId());
            getResponse.setContent(comment.getContent());
            getResponse.setAvatar(comment.getAvatar());
            return getResponse;
        }).toList();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        logger.info("Updating comment");
        Comment existingComment = commentService.getCommentById(commentId);
        if (existingComment == null) {
            return ResponseEntity.notFound().build();
        }
        existingComment.setContent(comment.getContent());
        commentService.saveComment(existingComment);
        return ResponseEntity.ok(existingComment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        logger.info("Deleting comment");
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        commentService.deleteComment(comment);
        return ResponseEntity.noContent().build();
    }

}
