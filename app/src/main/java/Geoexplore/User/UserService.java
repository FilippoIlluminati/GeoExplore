package Geoexplore.User;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Ottiene tutti gli utenti
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Trova un utente per ID
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Salva un nuovo utente
    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    // Aggiorna un utente esistente
    public Users updateUser(Long id, Users userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setNome(userDetails.getNome());
            user.setCognome(userDetails.getCognome());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getUsername());
            user.setRuolo(userDetails.getRuolo());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Elimina un utente
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
