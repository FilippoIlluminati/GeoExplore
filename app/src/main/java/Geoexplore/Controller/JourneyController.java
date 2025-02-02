package Geoexplore.Controller;

import Geoexplore.Journey.Journey;
import Geoexplore.Journey.JourneyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journeys")
public class JourneyController {

    private final JourneyManager journeyManager;

    @Autowired
    public JourneyController(JourneyManager journeyManager) {
        this.journeyManager = journeyManager;
    }

    // Crea un nuovo Journey
    @PostMapping
    public ResponseEntity<Journey> createJourney(@RequestBody Journey journey) {
        Journey savedJourney = journeyManager.saveJourney(journey);
        return ResponseEntity.ok(savedJourney);
    }

    // Recupera tutti i Journey
    @GetMapping
    public ResponseEntity<List<Journey>> getAllJourneys() {
        List<Journey> journeys = journeyManager.getAllJourneys();
        return ResponseEntity.ok(journeys);
    }

    // Recupera un Journey per ID
    @GetMapping("/{id}")
    public ResponseEntity<Journey> getJourneyById(@PathVariable Long id) {
        Optional<Journey> journey = journeyManager.getJourneyById(id);
        return journey.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna un Journey esistente
    @PutMapping("/{id}")
    public ResponseEntity<Journey> updateJourney(@PathVariable Long id, @RequestBody Journey journeyDetails) {
        try {
            Journey updatedJourney = journeyManager.updateJourney(id, journeyDetails);
            return ResponseEntity.ok(updatedJourney);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Elimina un Journey per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJourney(@PathVariable Long id) {
        if (journeyManager.getJourneyById(id).isPresent()) {
            journeyManager.deleteJourney(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Endpoint per confermare un Journey
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Journey> confirmJourney(@PathVariable Long id) {
        try {
            Journey confirmedJourney = journeyManager.confirmJourney(id);
            return ResponseEntity.ok(confirmedJourney);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
