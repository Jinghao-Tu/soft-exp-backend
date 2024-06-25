package backend.destination;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import backend.user.User;

@Data
@NoArgsConstructor
@Entity
@Table(name = "destinations")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String departureDate;

    @ElementCollection
    @Column(nullable = false)
    private List<String> checkboxValues;

    @Column(nullable = false)
    private String priceRange;

    @Column(nullable = false)
    private String companionRequirements;

    @Column(nullable = false)
    private String remark;
}
