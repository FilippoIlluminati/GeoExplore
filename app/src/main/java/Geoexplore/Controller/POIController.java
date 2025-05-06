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

    // Restituisce l'elenco completo dei POI
    @GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        List<POI> pois = poiService.getAllPOIs();
        return ResponseEntity.ok(pois);
    }

    // Restituisce un POI specifico tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        Optional<POI> poiOpt = poiService.getPOIById(id);
        return poiOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crea un nuovo POI (accesso riservato a utenti autenticati autorizzati)
    @PostMapping
    public ResponseEntity<?> createPOI(@RequestBody POI poi) {
        try {
            POI createdPOI = poiService.createPOI(poi);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPOI);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante la creazione del POI: " + e.getMessage());
        }
    }

    // Aggiorna un POI esistente (solo il creatore può modificarlo)
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePOI(@PathVariable Long id, @RequestBody POI poi) {
        try {
            POI updatedPOI = poiService.updatePOI(id, poi);
            return ResponseEntity.ok(updatedPOI);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Errore durante l'aggiornamento del POI: " + e.getMessage());
        }
    }

    // Elimina un POI esistente (solo il creatore può eliminarlo)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        try {
            poiService.deletePOI(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Approvazione di un POI (accesso riservato al Curatore)
    @PutMapping("/{id}/approve")
    public ResponseEntity<POI> approvePOI(@PathVariable Long id) {
        try {
            POI approvedPOI = poiService.approvePOI(id);
            return ResponseEntity.ok(approvedPOI);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Rifiuto di un POI (accesso riservato al Curatore, comporta l'eliminazione)
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectPOI(@PathVariable Long id) {
        try {
            poiService.rejectPOI(id);
            return ResponseEntity.ok("POI rifiutato ed eliminato.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Errore durante il rifiuto del POI: " + e.getMessage());
        }
    }

    // Restituisce i POI salvati dall’utente autenticato (Turista)
    @GetMapping("/saved")
    public ResponseEntity<?> getSavedPOIs() {
        try {
            Set<POI> savedPois = poiService.getSavedPOIsForTurista();
            return ResponseEntity.ok(savedPois);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante il recupero dei POI salvati: " + e.getMessage());
        }
    }
}
