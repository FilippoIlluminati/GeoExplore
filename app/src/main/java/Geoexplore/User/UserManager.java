package Geoexplore.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {

    private final UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Crea un nuovo utente
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    // Recupera tutti gli utenti
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Recupera un utente per ID
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Elimina un utente
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Aggiorna i dati di un utente
    public Users updateUser(Long id, Users updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            // Aggiorna i campi solo se sono stati forniti
            if (updatedUser.getNome() != null && !updatedUser.getNome().isEmpty()) {
                existingUser.setNome(updatedUser.getNome());
            }
            if (updatedUser.getCognome() != null && !updatedUser.getCognome().isEmpty()) {
                existingUser.setCognome(updatedUser.getCognome());
            }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getRuolo() != null) {
                existingUser.setRuolo(updatedUser.getRuolo());
            }

            // Salva l'utente aggiornato
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}
