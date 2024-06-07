package backend.post;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import backend.comments.Comment;
import backend.user.User;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private List<PostImage> images;

    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
