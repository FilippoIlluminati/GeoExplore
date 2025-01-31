package Geoexplore.Controller;

import Geoexplore.Contest.Contest;
import Geoexplore.Contest.ContestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Geoexplore.Contest.ContestService;
import Geoexplore.Contest.ContestStatus;
import java.util.Map; 


import java.util.List;
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateContestStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            // Legge il valore dallo JSON
            String newStatus = body.get("status");

            // Converte la stringa in enum ContestStatus
            ContestStatus status = ContestStatus.valueOf(newStatus.trim().toUpperCase());

            // Aggiorna lo stato del contest
            Contest updatedContest = contestService.updateContestStatus(id, status);
            return ResponseEntity.ok(updatedContest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Errore: Status non valido. Usa: APERTO, CHIUSO, IN_REVISIONE");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
        }
    }



}

