package Geoexplore.Controller;

import Geoexplore.Journey.Journey;
import Geoexplore.Journey.JourneyService;
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
import java.util.Optional;

@RestController
@RequestMapping("/journeys")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @Autowired
    private UserRepository userRepository;

    // Crea un nuovo itinerario associato all'utente autenticato
    @PostMapping
    public ResponseEntity<?> createJourney(
            @RequestBody Journey journey,
            @AuthenticationPrincipal UserDetails principal) {
        Users creator = userRepository.findByUsername(principal.getUsername());
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
        journey.setCreator(creator);
        try {
            Journey savedJourney = journeyService.createJourney(journey);
            return ResponseEntity.ok(savedJourney);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante la creazione: " + e.getMessage());
        }
    }

    // Restituisce tutti gli itinerari
    @GetMapping
    public ResponseEntity<List<Journey>> getAllJourneys(
            @AuthenticationPrincipal UserDetails principal) {
        List<Journey> journeys = journeyService.getAllJourneys();
        return ResponseEntity.ok(journeys);
    }

    // Restituisce un itinerario specifico per ID
    @GetMapping("/{id}")
    public ResponseEntity<Journey> getJourneyById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        Optional<Journey> journey = journeyService.getJourneyById(id);
        return journey.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna un itinerario esistente (solo creatore o ruoli specifici)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJourney(
            @PathVariable Long id,
            @RequestBody Journey journeyDetails,
            @AuthenticationPrincipal UserDetails principal) {
        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
        try {
            Journey updatedJourney = journeyService.updateJourney(id, journeyDetails);
            return ResponseEntity.ok(updatedJourney);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'aggiornamento: " + e.getMessage());
        }
    }

    // Elimina un itinerario (solo creatore o ruoli specifici)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJourney(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }
        try {
            journeyService.deleteJourney(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    // Approvazione di un itinerario (solo Curatore)
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveJourney(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        Users curator = userRepository.findByUsername(principal.getUsername());
        if (curator == null || curator.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accesso negato.");
        }
        try {
            Journey approvedJourney = journeyService.approveJourney(id);
            return ResponseEntity.ok(approvedJourney);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'approvazione: " + e.getMessage());
        }
    }

    // Rifiuto di un itinerario (solo Curatore)
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectJourney(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        Users curator = userRepository.findByUsername(principal.getUsername());
        if (curator == null || curator.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accesso negato.");
        }
        try {
            journeyService.rejectJourney(id);
            return ResponseEntity.ok("Itinerario rifiutato ed eliminato.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante il rifiuto: " + e.getMessage());
        }
    }
}
