package backend.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import backend.post.Post;
import backend.post.PostService;
import backend.user.User;
import backend.user.UserService;

public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
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
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        logger.info("Adding comment");
        commentService.saveComment(comment);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/allcomments")
    public ResponseEntity<List<Comment>> getAllComments() {
        logger.info("Getting all comments");
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        logger.info("Getting comments by post");
        Post post = postService.getPostById(postId);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = commentService.getCommentsByPost(post);
        return ResponseEntity.ok(comments);
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
