package backend.comments;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import backend.user.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import backend.post.Post;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String avatar;
}
