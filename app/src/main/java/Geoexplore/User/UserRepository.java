package Geoexplore.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    // Metodo per trovare un utente tramite il suo username
    Users findByUsername(String username);

    // Metodo per trovare un utente tramite la sua email
    Users findByEmail(String email);
}
