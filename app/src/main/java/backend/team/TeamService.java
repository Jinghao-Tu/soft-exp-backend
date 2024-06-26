package backend.team;

import backend.user.User;
import backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public void addMember(Team team, User user) {
        team.getMembers().add(user);
        teamRepository.save(team);
    }

    public Team findByUser(User user) {
        return teamRepository.findByMembersContaining(user);
    }

    public Team findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return teamRepository.findByMembersContaining(user);
        }
        return null;
    }

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    public void removeUserFromTeam(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Team team = teamRepository.findByMembersContaining(user);
            if (team != null) {
                team.getMembers().remove(user);
                teamRepository.save(team);
            }
        }
    }
}
