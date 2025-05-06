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

    // Endpoint per la registrazione di un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        try {
            // Il ruolo TURISTA non richiede registrazione
            if (user.getRuolo() == UserRole.TURISTA) {
                return ResponseEntity.badRequest().body("Registrazione non necessaria per il ruolo Turista.");
            }

            // Ruoli riservati non possono registrarsi autonomamente
            if (user.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                    user.getRuolo() == UserRole.ANIMATORE ||
                    user.getRuolo() == UserRole.CURATORE ||
                    user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
                return ResponseEntity.badRequest().body("Non puoi auto-registrarti come " + user.getRuolo() + ".");
            }

            // Registrazione come CONTRIBUTOR: account in attesa di approvazione
            if (user.getRuolo() == UserRole.CONTRIBUTOR) {
                user.setAccountStatus(AccountStatus.IN_ATTESA);
                authenticationService.register(user);
                return ResponseEntity.ok("Registrazione completata. Il tuo account è in attesa di approvazione.");
            }

            // Registrazione come TURISTA_AUTENTICATO: attivo immediatamente
            else if (user.getRuolo() == UserRole.TURISTA_AUTENTICATO) {
                user.setAccountStatus(AccountStatus.ATTIVO);
                authenticationService.register(user);
                return ResponseEntity.ok("Registrazione completata. Puoi accedere subito.");
            }

            return ResponseEntity.badRequest().body("Ruolo non valido per la registrazione.");

        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Errore durante la registrazione: " + ex.getMessage());
        }
    }

    // Endpoint per il login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Users user = authenticationService.getUserByUsername(username);

        if (user != null && user.getAccountStatus() == AccountStatus.ATTIVO) {
            boolean isAuthenticated = authenticationService.authenticate(username, password);
            return isAuthenticated
                    ? ResponseEntity.ok("Accesso effettuato con successo.")
                    : ResponseEntity.status(401).body("Credenziali non valide.");
        } else {
            return ResponseEntity.status(403).body("Accesso negato: account non attivo o in attesa di approvazione.");
        }
    }
}
