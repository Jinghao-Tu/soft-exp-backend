package backend.invitation;

import backend.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User to;

    @Column(nullable = false)
    private String status; // pending, accepted, declined
}
