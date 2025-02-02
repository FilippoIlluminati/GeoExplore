package Geoexplore.Controller;


import Geoexplore.POI.POI;
import Geoexplore.POI.POIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/poi")
public class POIController {

    @Autowired
    private POIService poiService;

    // Endpoint per ottenere tutti i POI
    @GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        List<POI> pois = poiService.getAllPOIs();
        return ResponseEntity.ok(pois);
    }

    // Endpoint per ottenere un POI tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        Optional<POI> poiOpt = poiService.getPOIById(id);
        return poiOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per creare un nuovo POI
    @PostMapping
    public ResponseEntity<POI> createPOI(@RequestBody POI poi) {
        try {
            POI createdPOI = poiService.createPOI(poi);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPOI);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint per aggiornare un POI esistente
    @PutMapping("/{id}")
    public ResponseEntity<POI> updatePOI(@PathVariable Long id, @RequestBody POI poi) {
        try {
            POI updatedPOI = poiService.updatePOI(id, poi);
            return ResponseEntity.ok(updatedPOI);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint per eliminare un POI
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        poiService.deletePOI(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint per approvare un POI (da richiamare, ad esempio, dal ruolo Curatore)
    @PutMapping("/{id}/approve")
    public ResponseEntity<POI> approvePOI(@PathVariable Long id) {
        try {
            POI approvedPOI = poiService.approvePOI(id);
            return ResponseEntity.ok(approvedPOI);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
