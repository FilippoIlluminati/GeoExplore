package Geoexplore.Controller;

import Geoexplore.User.UserRole;
import Geoexplore.User.UserService;
import Geoexplore.User.Users;
import Geoexplore.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Crea un nuovo utente (solo il Gestore può farlo)
    @PostMapping("/create-user")
    public ResponseEntity<?> createUserByGestore(
            @RequestBody Users user,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Richiedente non trovato."));

        Users createdUser = userService.createUserByGestore(user, requester);
        return ResponseEntity.ok("Utente creato correttamente: " + createdUser.getUsername());
    }

    // Approvazione di un contributor da parte del Gestore
    @PatchMapping("/approve-contributor/{id}")
    public ResponseEntity<?> approveContributorByGestore(
            @PathVariable Long id,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Richiedente non trovato."));

        Users approvedUser = userService.approveContributorByGestore(id, requester);
        return ResponseEntity.ok("Contributor approvato: " + approvedUser.getUsername());
    }

    // Restituisce l'elenco completo degli utenti
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Elimina un utente (solo il Gestore può farlo, non può eliminare se stesso)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserByGestore(
            @PathVariable Long id,
            @RequestParam Long managerId) {

        Users manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Gestore non trovato."));

        if (manager.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).body("Accesso negato: solo il Gestore può eliminare utenti.");
        }

        if (manager.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Operazione non consentita: il Gestore non può eliminare se stesso.");
        }

        if (userService.getUserById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
