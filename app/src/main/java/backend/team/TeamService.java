package backend.team;

import backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

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

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }
}
