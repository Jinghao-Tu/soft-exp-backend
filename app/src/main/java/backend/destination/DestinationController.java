package backend.destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import backend.user.UserService;
import backend.user.User;
import backend.destination.request.AddRequest;

@RestController
@RequestMapping("/api")
public class DestinationController {
    
    private static final Logger logger = LoggerFactory.getLogger(DestinationController.class);
    
    private final DestinationService destinationService;
    private final UserService userService;
    
    @Autowired
    public DestinationController(DestinationService destinationService, UserService userService) {
        this.destinationService = destinationService;
        this.userService = userService;
    }

    @GetMapping("/{username}/destinations")
    public List<Destination> getDestinationsByUser(@PathVariable String username) {
        logger.info("Getting destinations by user: " + username);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return null;
        }
        return destinationService.getDestinationsByUser(user);
    }

    @PostMapping("/destinations")
    public ResponseEntity<Destination> addDestination(@RequestBody AddRequest request) {
        logger.info("Adding destination for user: " + request.getUsername());
        User user = userService.getUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Destination destination = new Destination();
        destination.setUser(user);
        destination.setDeparture(request.getDeparture());
        destination.setDestination(request.getDestination());
        destination.setDepartureDate(request.getDepartureDate());
        destination.setCheckboxValues(request.getCheckboxValues());
        destination.setPriceRange(request.getPriceRange());
        destination.setCompanionRequirements(request.getCompanionRequirements());
        destination.setRemark(request.getRemark());
        destinationService.saveDestination(destination);
        return ResponseEntity.ok(destination);
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<Destination>> getAllDestinations() {
        logger.info("Getting all destinations");
        List<Destination> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/destinations/{username}")
    public ResponseEntity<List<Destination>> getDestinationsByUsername(@PathVariable String username) {
        logger.info("Getting destinations by username: " + username);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Destination> destinations = destinationService.getDestinationsByUser(user);
        return ResponseEntity.ok(destinations);
    }

    @PutMapping("/destinations/{id}")
    public ResponseEntity<Destination> updateDestination(@PathVariable Long id, @RequestBody AddRequest request) {
        logger.info("Updating destination with id: " + id);
        Destination destination = destinationService.getDestinationById(id);
        if (destination == null) {
            return ResponseEntity.notFound().build();
        }
        destination.setDeparture(request.getDeparture());
        destination.setDestination(request.getDestination());
        destination.setDepartureDate(request.getDepartureDate());
        destination.setCheckboxValues(request.getCheckboxValues());
        destination.setPriceRange(request.getPriceRange());
        destination.setCompanionRequirements(request.getCompanionRequirements());
        destination.setRemark(request.getRemark());
        destinationService.saveDestination(destination);
        return ResponseEntity.ok(destination);
    }

    @DeleteMapping("/destinations/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        logger.info("Deleting destination with id: " + id);
        Destination destination = destinationService.getDestinationById(id);
        if (destination == null) {
            return ResponseEntity.notFound().build();
        }
        destinationService.deleteDestination(destination);
        return ResponseEntity.noContent().build();
    }
}
