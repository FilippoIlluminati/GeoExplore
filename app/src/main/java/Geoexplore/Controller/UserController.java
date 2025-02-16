package Geoexplore.Controller;

import Geoexplore.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Creazione utente specifico (Solo Gestore della Piattaforma)
    @PostMapping("/create-user")
    public ResponseEntity<?> createUserByGestore(
            @RequestBody Users user,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Requester non trovato"));

        Users createdUser = userService.createUserByGestore(user, requester);
        return ResponseEntity.ok("Utente creato con successo: " + createdUser.getUsername());
    }

    // Approvazione contributor (Solo Gestore della Piattaforma)
    @PatchMapping("/approve-contributor/{id}")
    public ResponseEntity<?> approveContributorByGestore(
            @PathVariable Long id,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Requester non trovato"));

        Users approvedUser = userService.approveContributorByGestore(id, requester);
        return ResponseEntity.ok("Contributor approvato: " + approvedUser.getUsername());
    }

    // Endpoint per ottenere tutti gli utenti
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Endpoint per eliminare un utente (Solo Gestore della Piattaforma)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @RequestParam Long requesterId) {

        Users requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new SecurityException("Requester non trovato"));

        // Controllo: il gestore non può eliminarsi da solo
        if (requester.getId().equals(id)) {
            return ResponseEntity.status(400).body("Il gestore non può eliminarsi da solo.");
        }

        // Verifica che il richiedente sia il Gestore della Piattaforma
        if (requester.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).body("Solo il Gestore della piattaforma può eliminare un utente.");
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("Utente eliminato con successo.");
    }
}
