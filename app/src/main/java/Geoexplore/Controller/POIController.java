package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIService;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/poi")
public class POIController {

    @Autowired private POIService      poiService;
    @Autowired private UserRepository  userRepository;

    @GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        return ResponseEntity.ok(poiService.getAllPOIs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        Optional<POI> poiOpt = poiService.getPOIById(id);
        return poiOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<POI> createPOI(
            @RequestBody POI poi,
            @AuthenticationPrincipal UserDetails principal) {

        Users creator = userRepository.findByUsername(principal.getUsername());
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        poi.setCreator(creator);
        POI saved = poiService.createPOI(poi);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<POI> updatePOI(
            @PathVariable Long id,
            @RequestBody POI poi) {
        // POIService gestisce internamente l’utente autenticato
        POI updated = poiService.updatePOI(id, poi);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        poiService.deletePOI(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<POI> approvePOI(@PathVariable Long id) {
        return ResponseEntity.ok(poiService.approvePOI(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectPOI(@PathVariable Long id) {
        poiService.rejectPOI(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saved")
    public ResponseEntity<Set<POI>> getSavedPOIs() {
        return ResponseEntity.ok(poiService.getSavedPOIsForTurista());
    }
}
