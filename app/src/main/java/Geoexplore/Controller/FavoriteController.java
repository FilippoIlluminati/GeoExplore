package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private POIRepository poiRepository;

    // Aggiunge un POI alla lista dei preferiti dell'utente autenticato
    @PostMapping("/{poiId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long poiId, Authentication authentication) {
        // Recupera il nome utente dall'Authentication
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Verifica l'esistenza del POI
        Optional<POI> poiOpt = poiRepository.findById(poiId);
        if (!poiOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POI not found");
        }
        POI poi = poiOpt.get();

        // Aggiunge il POI ai preferiti
        user.getSavedPois().add(poi);
        userRepository.save(user);

        return ResponseEntity.ok("POI added to favorites");
    }

    // Recupera la lista dei POI salvati come preferiti dell'utente autenticato
    @GetMapping
    public ResponseEntity<Set<POI>> getFavorites(Authentication authentication) {
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Set<POI> favorites = user.getSavedPois();
        return ResponseEntity.ok(favorites);
    }
}
