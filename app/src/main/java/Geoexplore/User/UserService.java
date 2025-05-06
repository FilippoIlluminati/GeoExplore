package Geoexplore.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users createUserByGestore(Users user, Users requester) {
        if (requester.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new SecurityException("Solo il Gestore della piattaforma può creare utenti specifici.");
        }

        if (user.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                user.getRuolo() == UserRole.ANIMATORE ||
                user.getRuolo() == UserRole.CURATORE ||
                user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
            user.setAccountStatus(AccountStatus.ATTIVO);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
