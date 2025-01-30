package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/poi")
public class POIController {

    private final POIManager poiManager;

    @Autowired
    public POIController(POIManager poiManager) {
        this.poiManager = poiManager;
    }

    // Crea un nuovo POI
    @PostMapping
    public ResponseEntity<POI> createPOI(@RequestBody POI poi) {
        POI savedPOI = poiManager.savePOI(poi);
        return ResponseEntity.ok(savedPOI);
    }

    // Recupera tutti i POI
    @GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        List<POI> pois = poiManager.getAllPOIs();
        return ResponseEntity.ok(pois);
    }

    // Recupera un POI per ID
    @GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        Optional<POI> poi = poiManager.getPOIById(id);
        return poi.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un POI per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        if (poiManager.getPOIById(id).isPresent()) {
            poiManager.deletePOI(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
