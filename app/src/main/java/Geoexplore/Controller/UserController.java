package Geoexplore.Controller;

import Geoexplore.User.Users;
import Geoexplore.User.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    // Crea un nuovo utente - Accessibile solo al Gestore Piattaforma
    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users savedUser = userManager.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Recupera tutti gli utenti - Accessibile al Gestore Piattaforma e al Curatore
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGE_USERS', 'VIEW_USERS')")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userManager.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Recupera un utente per ID - Accessibile al Gestore Piattaforma e al Curatore
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGE_USERS', 'VIEW_USERS')")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> user = userManager.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un utente per ID - Accessibile solo al Gestore Piattaforma
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<Users> user = userManager.getUserById(id);
        if (user.isPresent()) {
            userManager.deleteUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Aggiorna un utente per ID - Accessibile solo al Gestore Piattaforma
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        Optional<Users> user = userManager.getUserById(id);
        if (user.isPresent()) {
            Users savedUser = userManager.updateUser(id, updatedUser);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
