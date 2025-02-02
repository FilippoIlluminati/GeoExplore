package Geoexplore.Controller;

import Geoexplore.Contest.Contest;
import Geoexplore.Contest.ContestManager;
import Geoexplore.Contest.ContestService;
import Geoexplore.Contest.ContestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contests")
public class ContestController {

    private final ContestManager contestManager;
    private final ContestService contestService;

    @Autowired
    public ContestController(ContestManager contestManager, ContestService contestService) {
        this.contestManager = contestManager;
        this.contestService = contestService;
    }

    // Crea un nuovo contest
    @PostMapping
    public ResponseEntity<?> createContest(@RequestBody Contest contest) {
        try {
            Contest savedContest = contestService.createContest(contest);
            return ResponseEntity.ok(savedContest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Recupera tutti i contest
    @GetMapping
    public ResponseEntity<List<Contest>> getAllContests() {
        List<Contest> contests = contestManager.getAllContests();
        return ResponseEntity.ok(contests);
    }

    // Recupera un contest per ID
    @GetMapping("/{id}")
    public ResponseEntity<Contest> getContestById(@PathVariable Long id) {
        Optional<Contest> contest = contestManager.getContestById(id);
        return contest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna un contest esistente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContest(@PathVariable Long id, @RequestBody Contest contest) {
        try {
            Contest updatedContest = contestService.updateContest(id, contest);
            return ResponseEntity.ok(updatedContest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Aggiorna lo stato di un contest
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateContestStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String newStatus = body.get("status");
            ContestStatus status = ContestStatus.valueOf(newStatus.trim().toUpperCase());
            Contest updatedContest = contestService.updateContestStatus(id, status);
            return ResponseEntity.ok(updatedContest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Errore: Status non valido. Usa: APERTO, CHIUSO, IN_REVISIONE");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Registra un partecipante a un contest
    @PostMapping("/{contestId}/register/{userId}")
    public ResponseEntity<?> registerParticipant(@PathVariable Long contestId, @PathVariable Long userId) {
        try {
            Contest contest = contestService.registerParticipant(contestId, userId);
            return ResponseEntity.ok(contest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Elimina un contest
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        if (contestManager.getContestById(id).isPresent()) {
            contestManager.deleteContest(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
