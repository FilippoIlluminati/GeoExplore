package Geoexplore.Config;

import Geoexplore.User.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("gestore") == null) {
                Users gestore = new Users(
                        "Admin",
                        "Piattaforma",
                        "gestore@example.com",
                        "gestore",
                        new BCryptPasswordEncoder().encode("password123"),
                        UserRole.GESTORE_PIATTAFORMA,
                        AccountStatus.ATTIVO
                );
                userRepository.save(gestore);
                System.out.println("✅ Gestore creato con username: gestore e password: password123");
            }
        };
    }
}
