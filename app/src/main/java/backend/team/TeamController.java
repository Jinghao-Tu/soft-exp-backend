package backend.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeam(@PathVariable Long teamId) {
        return teamService.findById(teamId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Team> getTeamByUsername(@PathVariable String username) {
        Team team = teamService.findByUsername(username);
        if (team != null) {
            return ResponseEntity.ok(team);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/leaveTeam")
    public ResponseEntity<?> leaveTeam(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        try {
            teamService.removeUserFromTeam(username);
            return ResponseEntity.ok().body("Successfully left the team");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to leave the team");
        }
    }

}
