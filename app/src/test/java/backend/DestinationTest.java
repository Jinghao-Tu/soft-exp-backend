package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import backend.destination.*;
import backend.destination.http.*;
import backend.user.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DestinationTest {

    @Autowired
    private DestinationRepository destinationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DestinationService destinationService = new DestinationService(destinationRepository);
    
    @Autowired
    private UserService userService = new UserService(userRepository);

    @Autowired
    private DestinationController destinationController = new DestinationController(destinationService, userService);

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void DestinationClassTest() {
        // create a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setHobby("Reading");
        // save user
        if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        } else {
            userService.updateUser(user.getUsername(), user);
        }
        // create a destination
        Destination destination = new Destination();
        destination.setUser(user);
        destination.setDeparture("departure");
        destination.setDestination("destination");
        destination.setDepartureDate("2021-12-31");
        destination.setCheckboxValues(List.of("checkboxValues"));
        destination.setPriceRange("priceRange");
        destination.setCompanionRequirements("companionRequirements");
        destination.setRemark("remark");
        // save destination
        destinationService.saveDestination(destination);
        // get destination
        List<Destination> destinations = destinationRepository.findByUser(user);
        // check if destination is saved
        Assertions.assertEquals(1, destinations.size());
        Destination newDestination = destinations.get(0);
        Assertions.assertEquals(destination.getUser().getUsername(), newDestination.getUser().getUsername());
        Assertions.assertEquals(destination.getDeparture(), newDestination.getDeparture());
        Assertions.assertEquals(destination.getDestination(), newDestination.getDestination());
        Assertions.assertEquals(destination.getDepartureDate(), newDestination.getDepartureDate());
        Assertions.assertEquals(destination.getCheckboxValues(), newDestination.getCheckboxValues());
        Assertions.assertEquals(destination.getPriceRange(), newDestination.getPriceRange());
        Assertions.assertEquals(destination.getCompanionRequirements(), newDestination.getCompanionRequirements());
        Assertions.assertEquals(destination.getRemark(), newDestination.getRemark());
        // delete destination
        destinationService.deleteDestinationById(newDestination.getId());
        // check if destination is deleted
        Assertions.assertEquals(0, destinationRepository.findByUser(user).size());
    }

    @Test
    @Transactional
    public void DestinationServiceTest() throws Exception {
        // create a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setHobby("Reading");
        // save user
        if (userService.getUserByUsername(user.getUsername()) == null) {
            userService.saveUser(user);
        } else {
            userService.updateUser(user.getUsername(), user);
        }
        // create a destination
        Destination destination = new Destination();
        destination.setUser(user);
        destination.setDeparture("departure");
        destination.setDestination("destination");
        destination.setDepartureDate("2021-12-31");
        destination.setCheckboxValues(List.of("checkboxValues"));
        destination.setPriceRange("priceRange");
        destination.setCompanionRequirements("companionRequirements");
        destination.setRemark("remark");
        // save destination
        destinationService.saveDestination(destination);
        // get destination
        List<Destination> destinations = destinationService.getDestinationsByUser(user);
        // check if destination is saved
        Assertions.assertEquals(1, destinations.size());
        Destination newDestination = destinations.get(0);
        Assertions.assertEquals(destination.getUser().getUsername(), newDestination.getUser().getUsername());
        Assertions.assertEquals(destination.getDeparture(), newDestination.getDeparture());
        Assertions.assertEquals(destination.getDestination(), newDestination.getDestination());
        Assertions.assertEquals(destination.getDepartureDate(), newDestination.getDepartureDate());
        Assertions.assertEquals(destination.getCheckboxValues(), newDestination.getCheckboxValues());
        Assertions.assertEquals(destination.getPriceRange(), newDestination.getPriceRange());
        Assertions.assertEquals(destination.getCompanionRequirements(), newDestination.getCompanionRequirements());
        Assertions.assertEquals(destination.getRemark(), newDestination.getRemark());
        // delete destination
        destinationService.deleteDestinationById(newDestination.getId());
        // check if destination is deleted
        Assertions.assertEquals(0, destinationService.getDestinationsByUser(user).size());
    }

    @Test
    @Transactional
    public void DestinationControllerTest() throws Exception {
        // create a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setHobby("Reading");
        // save user
        if (userService.getUserByUsername(user.getUsername()) == null) {
            userService.saveUser(user);
        } else {
            userService.updateUser(user.getUsername(), user);
        }
        // create a destination
        AddRequest addRequest = new AddRequest();
        addRequest.setUsername(user.getUsername());
        addRequest.setDeparture("departure");
        addRequest.setDestination("destination");
        addRequest.setDepartureDate("2021-12-31");
        addRequest.setCheckboxValues(List.of("checkboxValues"));
        addRequest.setPriceRange("priceRange");
        addRequest.setCompanionRequirements("companionRequirements");
        addRequest.setRemark("remark");
        // add destination
        mockMvc.perform(MockMvcRequestBuilders.post("/api/destinations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addRequest)))
                .andExpect(status().isOk());
        // get destination
        ResponseEntity<List<GetResponse>> response = destinationController.getAllDestinations();
        List<GetResponse> responses = response.getBody();
        // check if destination is empty    
        Assertions.assertNotNull(responses);
        // get destination by user
        List<Destination> destinations = destinationService.getDestinationsByUser(user);
        // check if destination is saved
        Assertions.assertEquals(1, destinations.size());
        Destination destination = destinations.get(0);
        GetResponse getResponse = responses.get(0);
        Assertions.assertEquals(destination.getUser().getUsername(), getResponse.getUsername());
        Assertions.assertEquals(destination.getDeparture(), getResponse.getDeparture());
        Assertions.assertEquals(destination.getDestination(), getResponse.getDestination());
        Assertions.assertEquals(destination.getDepartureDate(), getResponse.getDepartureDate());
        Assertions.assertEquals(destination.getCheckboxValues(), getResponse.getCheckboxValues());
        Assertions.assertEquals(destination.getPriceRange(), getResponse.getPriceRange());
        Assertions.assertEquals(destination.getCompanionRequirements(), getResponse.getCompanionRequirements());
        Assertions.assertEquals(destination.getRemark(), getResponse.getRemark());
        // delete destination
        destinationService.deleteDestinationById(destination.getId());
        // check if destination is deleted
        Assertions.assertEquals(0, destinationService.getDestinationsByUser(user).size());
    }

}
