package Geoexplore.Config;

import Geoexplore.User.AccountStatus;
import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.Journey.Journey;
import Geoexplore.Journey.JourneyRepository;
import Geoexplore.Content.ContentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository,
                                          POIRepository poiRepository,
                                          JourneyRepository journeyRepository,
                                          ContentRepository contentRepository) {
        return args -> {

            // Inizializzazione del gestore della piattaforma
            Users gestore = userRepository.findByUsername("gestore");
            if (gestore == null) {
                gestore = new Users(
                        "Admin",
                        "Piattaforma",
                        "gestore@example.com",
                        "gestore",
                        new BCryptPasswordEncoder().encode("password123"),
                        UserRole.GESTORE_PIATTAFORMA,
                        AccountStatus.ATTIVO
                );
                userRepository.save(gestore);
            }

            // Creazione di un POI
            POI poi = poiRepository.findByNome("Piazza del Duomo");
            if (poi == null) {
                poi = new POI();
                poi.setNome("Piazza del Duomo");
                poi.setDescrizione("Piazza storica con una magnifica vista del Duomo.");
                poi.setLatitude(43.123456);
                poi.setLongitude(12.654321);
                poi.setCategoria(Geoexplore.POI.Category.STORICO);
                poi.setComune("Milano");
                poi.setCreator(gestore);
                poi.setApprovato(true);
                poiRepository.save(poi);
            }

            // Creazione di un itinerario
            Journey journey = journeyRepository.findByNome("Itinerario Storico");
            if (journey == null) {
                journey = new Journey();
                journey.setNome("Itinerario Storico");
                journey.setDescrizione("Un percorso che attraversa monumenti storici di Milano.");
                journey.setCreator(gestore);
                journey.setOrdinato(true);
                journey.setPoiList(List.of(poi));
                journeyRepository.save(journey);
            }
        };
    }
}
