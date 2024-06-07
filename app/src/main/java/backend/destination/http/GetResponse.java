package backend.destination.http;

import lombok.Data;
import java.util.List;

@Data
public class GetResponse {
    private Long id;
    private String username;
    private String departure;
    private String destination;
    private String departureDate;
    private List<String> checkboxValues;
    private String priceRange;
    private String companionRequirements;
    private String remark;
}
