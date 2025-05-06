package Geoexplore.Controller;

import Geoexplore.Journey.Journey;
import Geoexplore.Journey.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journeys")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    // Crea un nuovo itinerario
    @PostMapping
    public ResponseEntity<?> createJourney(@RequestBody Journey journey) {
        try {
            Journey savedJourney = journeyService.createJourney(journey);
            return ResponseEntity.ok(savedJourney);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante la creazione: " + e.getMessage());
        }
    }

    // Restituisce tutti gli itinerari
    @GetMapping
    public ResponseEntity<List<Journey>> getAllJourneys() {
        List<Journey> journeys = journeyService.getAllJourneys();
        return ResponseEntity.ok(journeys);
    }

    // Restituisce un itinerario specifico per ID
    @GetMapping("/{id}")
    public ResponseEntity<Journey> getJourneyById(@PathVariable Long id) {
        Optional<Journey> journey = journeyService.getJourneyById(id);
        return journey.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna un itinerario esistente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJourney(@PathVariable Long id, @RequestBody Journey journeyDetails) {
        try {
            Journey updatedJourney = journeyService.updateJourney(id, journeyDetails);
            return ResponseEntity.ok(updatedJourney);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'aggiornamento: " + e.getMessage());
        }
    }

    // Elimina un itinerario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJourney(@PathVariable Long id) {
        try {
            journeyService.deleteJourney(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    // Approvazione di un itinerario (solo curatore)
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveJourney(@PathVariable Long id) {
        try {
            Journey approvedJourney = journeyService.approveJourney(id);
            return ResponseEntity.ok(approvedJourney);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante l'approvazione: " + e.getMessage());
        }
    }

    // Rifiuto di un itinerario (solo curatore, il viaggio viene eliminato)
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectJourney(@PathVariable Long id) {
        try {
            journeyService.rejectJourney(id);
            return ResponseEntity.ok("Itinerario rifiutato ed eliminato.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante il rifiuto: " + e.getMessage());
        }
    }
}
