package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/poi")
public class POIController {

    @Autowired
    private POIService poiService;

    @GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        List<POI> pois = poiService.getAllPOIs();
        return ResponseEntity.ok(pois);
    }

    @GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        Optional<POI> poiOpt = poiService.getPOIById(id);
        return poiOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per la creazione del POI
    @PostMapping
    public ResponseEntity<?> createPOI(@RequestBody POI poi) {
        try {
            POI createdPOI = poiService.createPOI(poi);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPOI);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint per l'aggiornamento del POI
    // (Solo il creatore del POI può modificarlo)
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePOI(@PathVariable Long id, @RequestBody POI poi) {
        try {
            POI updatedPOI = poiService.updatePOI(id, poi);
            return ResponseEntity.ok(updatedPOI);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint per l'eliminazione del POI
    // (Solo il creatore del POI può eliminarlo)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        try {
            poiService.deletePOI(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint per l'approvazione del POI (solo il curatore può approvare)
    @PutMapping("/{id}/approve")
    public ResponseEntity<POI> approvePOI(@PathVariable Long id) {
        try {
            POI approvedPOI = poiService.approvePOI(id);
            return ResponseEntity.ok(approvedPOI);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Nuovo endpoint per il rifiuto del POI (solo il curatore può rifiutare)
    // In caso di rifiuto il POI viene eliminato
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectPOI(@PathVariable Long id) {
        try {
            poiService.rejectPOI(id);
            return ResponseEntity.ok("POI rifiutato ed eliminato");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuovo endpoint: recupera la lista dei POI salvati dall'utente autenticato
    @GetMapping("/saved")
    public ResponseEntity<?> getSavedPOIs() {
        try {
            Set<POI> savedPois = poiService.getSavedPOIsForTurista();
            return ResponseEntity.ok(savedPois);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 }
