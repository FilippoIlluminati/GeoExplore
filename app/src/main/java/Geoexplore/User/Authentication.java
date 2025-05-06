package Geoexplore.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Authentication {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean register(Users user) {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere null o vuota.");
        }
        if (user.getRuolo() == UserRole.TURISTA ||
                user.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                user.getRuolo() == UserRole.ANIMATORE ||
                user.getRuolo() == UserRole.CURATORE ||
                user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
            throw new IllegalArgumentException("Registrazione non consentita per il ruolo: " + user.getRuolo());
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("L'utente con username \"" + user.getUsername() + "\" esiste già.");
        }
        if (user.getRuolo() == UserRole.CONTRIBUTOR) {
            user.setAccountStatus(AccountStatus.IN_ATTESA);
        } else if (user.getRuolo() == UserRole.TURISTA_AUTENTICATO) {
            user.setAccountStatus(AccountStatus.ATTIVO);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean authenticate(String username, String password) {
        Users user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
