package Geoexplore.Controller;

import Geoexplore.Contest.Contest;
import Geoexplore.Contest.ContestEntry;
import Geoexplore.Contest.ContestService;
import Geoexplore.Contest.StatoConcorso;
import Geoexplore.User.Users;
import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired private ContestService contestService;
    @Autowired private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> creaConcorso(@RequestBody Contest concorso,
                                          @RequestParam Long creatoreId) {
        try {
            Contest saved = contestService.creaConcorso(concorso, creatoreId);
            return ResponseEntity.ok("Concorso creato. ID: " + saved.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/{concorsoId}/join")
    public ResponseEntity<?> partecipa(@PathVariable Long concorsoId,
                                       @RequestParam Long partecipanteId,
                                       @RequestBody(required=false) ContestEntry participation) {
        try {
            ContestEntry saved = contestService.partecipaAlConcorso(
                    concorsoId, participation, partecipanteId);
            return ResponseEntity.ok("Partecipazione inviata. ID: " + saved.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Submit or update the contest entry content.
     * Now mapped to /contest/partecipazione/{parteId}/contenuto
     * so you can call:
     *   PUT http://localhost:8080/contest/partecipazione/{entryId}/contenuto?partecipanteId=...
     */
    @PutMapping("/partecipazione/{parteId}/contenuto")
    public ResponseEntity<?> submitContent(@PathVariable Long parteId,
                                           @RequestParam Long partecipanteId,
                                           @RequestBody Map<String,String> body) {
        try {
            String contenuto = body.get("contenuto");
            ContestEntry updated = contestService.submitContent(
                    parteId, contenuto, partecipanteId);
            return ResponseEntity.ok("Contenuto inviato. ID partecipazione: " + updated.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping("/partecipazione/{parteId}/approva")
    public ResponseEntity<?> approva(@PathVariable Long parteId,
                                     @RequestParam Long validatoreId) {
        try {
            ContestEntry upd = contestService.approvaPartecipazione(parteId, validatoreId);
            return ResponseEntity.ok("Partecipazione approvata. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/partecipazione/{parteId}/rifiuta")
    public ResponseEntity<?> rifiuta(@PathVariable Long parteId,
                                     @RequestParam Long validatoreId) {
        try {
            ContestEntry upd = contestService.rifiutaPartecipazione(parteId, validatoreId);
            return ResponseEntity.ok("Partecipazione rifiutata. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{concorsoId}/partecipazioni")
    public ResponseEntity<?> listPartecipazioni(@PathVariable Long concorsoId) {
        return ResponseEntity.ok(contestService.getPartecipazioniPerConcorso(concorsoId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(contestService.getAllConcorsi());
    }

    @GetMapping("/status")
    public ResponseEntity<?> byStatus(@RequestParam String stato) {
        try {
            StatoConcorso s = StatoConcorso.valueOf(stato);
            return ResponseEntity.ok(contestService.getConcorsiByStato(s));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Stato non valido.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contestService.getContestById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/animator/{animId}/partecipazioni")
    public ResponseEntity<?> byAnimator(@PathVariable Long animId) {
        return ResponseEntity.ok(contestService.getPartecipazioniPerAnimatore(animId));
    }
}
