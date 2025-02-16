package Geoexplore.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Recupera un utente per ID
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Recupera tutti gli utenti
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Creazione di un utente specifico (Solo Gestore della piattaforma)
    public Users createUserByGestore(Users user, Users requester) {
        if (requester.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new SecurityException("Solo il Gestore della piattaforma può creare utenti specifici.");
        }
        // Per ruoli riservati, imposta lo stato ATTIVO
        if (user.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                user.getRuolo() == UserRole.ANIMATORE ||
                user.getRuolo() == UserRole.CURATORE ||
                user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
            user.setAccountStatus(AccountStatus.ATTIVO);
        }
        return userRepository.save(user);
    }

    // Approvazione di un contributor (Solo Gestore della piattaforma)
    public Users approveContributorByGestore(Long userId, Users requester) {
        if (requester.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new SecurityException("Solo il Gestore della piattaforma può approvare utenti.");
        }
        return userRepository.findById(userId).map(user -> {
            if (user.getRuolo() == UserRole.CONTRIBUTOR && user.getAccountStatus() == AccountStatus.IN_ATTESA) {
                user.setAccountStatus(AccountStatus.ATTIVO);
                return userRepository.save(user);
            } else {
                throw new IllegalStateException("L'utente non è un CONTRIBUTOR in attesa.");
            }
        }).orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Eliminazione di un utente (Solo Gestore della piattaforma)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
