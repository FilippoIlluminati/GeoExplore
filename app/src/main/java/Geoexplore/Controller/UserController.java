package Geoexplore.Controller;

import Geoexplore.User.UserService;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import Geoexplore.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService    userService;
    @Autowired private UserRepository userRepository;

    @PostMapping("/create-user")
    public ResponseEntity<String> createUserByGestore(
            @RequestBody Users user,
            @AuthenticationPrincipal UserDetails principal) {

        Users mgr = userRepository.findByUsername(principal.getUsername());
        if (mgr == null || mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).body("Accesso negato");
        }
        Users created = userService.createUserByGestore(user, mgr);
        return ResponseEntity.ok("Utente creato: " + created.getUsername());
    }

    @PatchMapping("/approve-contributor/{id}")
    public ResponseEntity<String> approveContributor(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        Users mgr = userRepository.findByUsername(principal.getUsername());
        if (mgr == null || mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).body("Accesso negato");
        }
        Users approved = userService.approveContributorByGestore(id, mgr);
        return ResponseEntity.ok("Contributor approvato: " + approved.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserByGestore(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        Users mgr = userRepository.findByUsername(principal.getUsername());
        if (mgr == null
                || mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA
                || mgr.getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
