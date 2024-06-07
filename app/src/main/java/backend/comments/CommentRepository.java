package backend.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import backend.post.Post;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByPost(Post post);
}
