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

    // ——————————————————————————————————————————
    //  Endpoints esistenti (creazione, approvazione, get all)
    // ——————————————————————————————————————————

    @PostMapping("/create-user")
    public ResponseEntity<?> createUserByGestore(
            @RequestBody Users user,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Requester non trovato"));

        Users createdUser = userService.createUserByGestore(user, requester);
        return ResponseEntity.ok("Utente creato con successo: " + createdUser.getUsername());
    }

    @PatchMapping("/approve-contributor/{id}")
    public ResponseEntity<?> approveContributorByGestore(
            @PathVariable Long id,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Requester non trovato"));

        Users approvedUser = userService.approveContributorByGestore(id, requester);
        return ResponseEntity.ok("Contributor approvato: " + approvedUser.getUsername());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ——————————————————————————————————————————
    //  NUOVO: Eliminazione di un utente da parte del Gestore
    //  DELETE /users/{id}?managerId=...
    // ——————————————————————————————————————————
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserByGestore(
            @PathVariable Long id,
            @RequestParam Long managerId) {

        Users manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trovato"));

        if (manager.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).body("Solo il Gestore della piattaforma può eliminare utenti.");
        }

        // Protezione: il gestore non può eliminare se stesso
        if (manager.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Il Gestore non può eliminarsi da solo.");
        }

        // Verifico che l'utente esista
        if (userService.getUserById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Eliminazione effettiva
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
