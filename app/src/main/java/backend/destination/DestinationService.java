package backend.destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import backend.user.User;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getDestinationsByUser(User user) {
        return destinationRepository.findByUser(user);
    }

    public Destination saveDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(Long id) {
        return destinationRepository.findById(id).orElse(null);
    }

    public void deleteDestination(Destination destination) {
        destinationRepository.delete(destination);
    }

}
