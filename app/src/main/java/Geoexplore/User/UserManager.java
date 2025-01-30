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
}
