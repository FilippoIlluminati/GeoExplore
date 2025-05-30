package Geoexplore.Controller;

import Geoexplore.Contest.Contest;
import Geoexplore.Contest.ContestEntry;
import Geoexplore.Contest.ContestService;
import Geoexplore.Contest.StatoConcorso;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired private ContestService contestService;
    @Autowired private UserRepository userRepository;

    // Crea un nuovo concorso (solo Animatore)
    @PostMapping("/create")
    public ResponseEntity<?> creaConcorso(
            @RequestBody Contest concorso,
            @AuthenticationPrincipal UserDetails principal) {
        // Prendo l'utente autenticato
        Users creatore = userRepository.findByUsername(principal.getUsername());
        try {
            Contest saved = contestService.creaConcorso(concorso, creatore.getId());
            return ResponseEntity.ok("Concorso creato con successo. ID: " + saved.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Partecipa a un concorso
    @PostMapping("/{concorsoId}/join")
    public ResponseEntity<?> partecipa(
            @PathVariable Long concorsoId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody(required = false) ContestEntry participation) {

        Users partecipante = userRepository.findByUsername(principal.getUsername());
        try {
            ContestEntry saved = contestService.partecipaAlConcorso(
                    concorsoId, participation, partecipante.getId());
            return ResponseEntity.ok("Partecipazione inviata. ID: " + saved.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Invia o aggiorna il contenuto di una partecipazione
    @PutMapping("/partecipazione/{parteId}/contenuto")
    public ResponseEntity<?> submitContent(
            @PathVariable Long parteId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody Map<String, String> body) {

        Users partecipante = userRepository.findByUsername(principal.getUsername());
        try {
            String contenuto = body.get("contenuto");
            ContestEntry updated = contestService.submitContent(
                    parteId, contenuto, partecipante.getId());
            return ResponseEntity.ok("Contenuto inviato. ID partecipazione: " + updated.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Approvazione di una partecipazione (Animatore o Curatore)
    @PatchMapping("/partecipazione/{parteId}/approva")
    public ResponseEntity<?> approva(
            @PathVariable Long parteId,
            @AuthenticationPrincipal UserDetails principal) {

        Users validatore = userRepository.findByUsername(principal.getUsername());
        try {
            ContestEntry upd = contestService.approvaPartecipazione(
                    parteId, validatore.getId());
            return ResponseEntity.ok("Partecipazione approvata. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Rifiuto di una partecipazione (Animatore o Curatore)
    @PatchMapping("/partecipazione/{parteId}/rifiuta")
    public ResponseEntity<?> rifiuta(
            @PathVariable Long parteId,
            @AuthenticationPrincipal UserDetails principal) {

        Users validatore = userRepository.findByUsername(principal.getUsername());
        try {
            ContestEntry upd = contestService.rifiutaPartecipazione(
                    parteId, validatore.getId());
            return ResponseEntity.ok("Partecipazione rifiutata. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Restituisce tutte le partecipazioni a un concorso
    @GetMapping("/{concorsoId}/partecipazioni")
    public ResponseEntity<?> listPartecipazioni(@PathVariable Long concorsoId) {
        return ResponseEntity.ok(contestService.getPartecipazioniPerConcorso(concorsoId));
    }

    // Restituisce l'elenco di tutti i concorsi
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(contestService.getAllConcorsi());
    }

    // Restituisce concorsi filtrati per stato
    @GetMapping("/status")
    public ResponseEntity<?> byStatus(@RequestParam String stato) {
        try {
            StatoConcorso s = StatoConcorso.valueOf(stato);
            return ResponseEntity.ok(contestService.getConcorsiByStato(s));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Stato non valido.");
        }
    }

    // Restituisce un concorso specifico per ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contestService.getContestById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Restituisce tutte le partecipazioni ai concorsi creati dall'animatore autenticato
    @GetMapping("/animator/partecipazioni")
    public ResponseEntity<?> byAnimator(@AuthenticationPrincipal UserDetails principal) {
        Users animatore = userRepository.findByUsername(principal.getUsername());
        return ResponseEntity.ok(contestService.getPartecipazioniPerAnimatore(animatore.getId()));
    }
}
