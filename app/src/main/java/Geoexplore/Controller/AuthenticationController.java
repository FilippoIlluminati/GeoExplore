package Geoexplore.Controller;

import Geoexplore.User.Authentication;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import Geoexplore.User.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private Authentication authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        try {
            // 1) TURISTA non si registra
            if (user.getRuolo() == UserRole.TURISTA) {
                return ResponseEntity.badRequest().body("Registrazione non necessaria per il ruolo Turista.");
            }
            // 2) Ruoli riservati non si autoregistrano
            if (user.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                    user.getRuolo() == UserRole.ANIMATORE ||
                    user.getRuolo() == UserRole.CURATORE ||
                    user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
                return ResponseEntity.badRequest().body("Non puoi auto-registrarti come " + user.getRuolo());
            }
            // 3) CONTRIBUTOR -> stato IN_ATTESA
            if (user.getRuolo() == UserRole.CONTRIBUTOR) {
                user.setAccountStatus(AccountStatus.IN_ATTESA);
                authenticationService.register(user);
                return ResponseEntity.ok("Registrazione completata. L'account è in attesa di approvazione.");
            }
            // 4) TURISTA_AUTENTICATO -> stato ATTIVO
            else if (user.getRuolo() == UserRole.TURISTA_AUTENTICATO) {
                user.setAccountStatus(AccountStatus.ATTIVO);
                authenticationService.register(user);
                return ResponseEntity.ok("Registrazione completata. Puoi accedere subito.");
            }
            return ResponseEntity.badRequest().body("Ruolo non riconosciuto per la registrazione.");
        } catch (Exception ex) {
            // Se manca la password o c'è un utente duplicato, etc.
            return ResponseEntity.status(500).body("Errore durante la registrazione: " + ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Users user = authenticationService.getUserByUsername(username);
        if (user != null && user.getAccountStatus() == AccountStatus.ATTIVO) {
            boolean isAuthenticated = authenticationService.authenticate(username, password);
            return isAuthenticated ? ResponseEntity.ok("Accesso effettuato con successo!")
                    : ResponseEntity.status(401).body("Credenziali non valide.");
        } else {
            return ResponseEntity.status(403).body("L'account non è attivo o è in attesa.");
        }
    }
}
