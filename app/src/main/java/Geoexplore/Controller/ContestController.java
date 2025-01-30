package Geoexplore.Controller;

import Geoexplore.Contest.Contest;
import Geoexplore.Contest.ContestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contests")
public class ContestController {

    private final ContestManager contestManager;

    @Autowired
    public ContestController(ContestManager contestManager) {
        this.contestManager = contestManager;
    }

    // Crea un nuovo Contest
    @PostMapping
    public ResponseEntity<Contest> createContest(@RequestBody Contest contest) {
        Contest savedContest = contestManager.saveContest(contest);
        return ResponseEntity.ok(savedContest);
    }

    // Recupera tutti i Contest
    @GetMapping
    public ResponseEntity<List<Contest>> getAllContests() {
        List<Contest> contests = contestManager.getAllContests();
        return ResponseEntity.ok(contests);
    }

    // Recupera un Contest per ID
    @GetMapping("/{id}")
    public ResponseEntity<Contest> getContestById(@PathVariable Long id) {
        Optional<Contest> contest = contestManager.getContestById(id);
        return contest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un Contest per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        if (contestManager.getContestById(id).isPresent()) {
            contestManager.deleteContest(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
