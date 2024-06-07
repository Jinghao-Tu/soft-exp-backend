package backend.destination;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import backend.user.User;

@Data
@NoArgsConstructor
@Entity
@Table(name = "destinations")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String departureDate;

    @ElementCollection
    private List<String> checkboxValues;

    @Column(nullable = false)
    private String priceRange;

    @Column(nullable = false)
    private String companionRequirements;

    @Column(nullable = false)
    private String remark;
    
}
