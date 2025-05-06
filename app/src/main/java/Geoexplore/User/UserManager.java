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

    public Users saveUser(Users user, Users requester) {
        if (requester.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new SecurityException("Solo il Gestore della piattaforma può aggiungere utenti.");
        }
        return userRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
