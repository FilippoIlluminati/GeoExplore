package Geoexplore.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Authentication {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Metodo per registrare un nuovo utente
    public boolean register(Users user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false; // L'utente esiste già
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Cripta la password
        userRepository.save(user);
        return true;
    }

    // Metodo per autenticare un utente esistente
    public boolean authenticate(String username, String password) {
        Users user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    // Metodo per ottenere un utente tramite username
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
