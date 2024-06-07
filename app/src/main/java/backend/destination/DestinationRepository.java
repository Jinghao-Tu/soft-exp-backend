package backend.destination;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import backend.user.User;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByUser(User user);
}
