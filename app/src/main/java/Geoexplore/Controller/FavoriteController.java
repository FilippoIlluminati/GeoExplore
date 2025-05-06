package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private POIRepository poiRepository;

    // Aggiunge un POI ai preferiti dell’utente autenticato
    @PostMapping("/{poiId}")
    public ResponseEntity<?> addFavorite(
            @PathVariable Long poiId,
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        }

        Optional<POI> poiOpt = poiRepository.findById(poiId);
        if (!poiOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POI non trovato.");
        }

        POI poi = poiOpt.get();
        user.getSavedPois().add(poi);
        userRepository.save(user);

        return ResponseEntity.ok("POI aggiunto ai preferiti.");
    }

    // Restituisce i POI salvati tra i preferiti dall’utente autenticato
    @GetMapping
    public ResponseEntity<Set<POI>> getFavorites(
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Set<POI> favorites = user.getSavedPois();
        return ResponseEntity.ok(favorites);
    }
}
