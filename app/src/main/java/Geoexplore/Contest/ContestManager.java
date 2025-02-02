package Geoexplore.Contest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContestManager {

    private final ContestRepository contestRepository;

    @Autowired
    public ContestManager(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public Contest saveContest(Contest contest) {
        return contestRepository.save(contest);
    }

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public Optional<Contest> getContestById(Long id) {
        return contestRepository.findById(id);
    }

    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }
}
