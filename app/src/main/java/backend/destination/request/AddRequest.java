package backend.destination.request;

import java.util.List;
import lombok.Data;

@Data
public class AddRequest {
    // private Long id;
    private String username;
    private String departure;
    private String destination;
    private String departureDate;
    private List<String> checkboxValues;
    private String priceRange;
    private String companionRequirements;
    private String remark;
}
