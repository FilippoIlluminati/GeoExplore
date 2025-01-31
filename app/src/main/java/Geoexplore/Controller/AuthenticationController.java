package Geoexplore.Controller;

import Geoexplore.User.Authentication;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private Authentication authenticationService;

    // Endpoint per registrare un nuovo utente con JSON
    @PostMapping("/register")
    public String register(@RequestBody Users user) {
        boolean isRegistered = authenticationService.register(user);
        if (isRegistered) {
            return "Utente registrato con successo!";
        } else {
            return "Errore: L'utente esiste già!";
        }
    }

    // Endpoint per autenticare un utente
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = authenticationService.authenticate(username, password);
        if (isAuthenticated) {
            return "Accesso effettuato con successo!";
        } else {
            return "Errore: Credenziali non valide!";
        }
    }

    // Endpoint per recuperare o gestire la password di un utente
    @GetMapping("/getPassword")
    public String getPassword(@RequestParam String username) {
        Users user = authenticationService.getUserByUsername(username);
        if (user != null) {
            return "La password è protetta per motivi di sicurezza. Contatta l'amministratore per reimpostarla.";
        } else {
            return "Errore: Utente non trovato!";
        }
    }
}
