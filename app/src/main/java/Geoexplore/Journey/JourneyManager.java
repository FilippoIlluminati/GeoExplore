package Geoexplore.Journey;

import Geoexplore.User.UserRole;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JourneyManager {

    private final JourneyRepository journeyRepository;
    private final UserRepository userRepository;

    @Autowired
    public JourneyManager(JourneyRepository journeyRepository, UserRepository userRepository) {
        this.journeyRepository = journeyRepository;
        this.userRepository = userRepository;
    }

    public Journey saveJourney(Journey journey) {
        // Se il Journey ha un creator e l'id è presente, recuperiamo l'utente completo dal DB
        if (journey.getCreator() != null && journey.getCreator().getId() != null) {
            Optional<Users> optionalCreator = userRepository.findById(journey.getCreator().getId());
            if (optionalCreator.isPresent()) {
                Users creator = optionalCreator.get();
                journey.setCreator(creator);
                // Controlla il ruolo per impostare il flag confermato automaticamente
                if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                        creator.getRuolo() == UserRole.CURATORE ||
                        creator.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
                    journey.setConfermato(true);
                } else {
                    journey.setConfermato(false);
                }
            }
        }
        return journeyRepository.save(journey);
    }

    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    public Optional<Journey> getJourneyById(Long id) {
        return journeyRepository.findById(id);
    }

    public void deleteJourney(Long id) {
        journeyRepository.deleteById(id);
    }

    // Metodo per confermare un Journey manualmente (per chi non è autorizzato)
    public Journey confirmJourney(Long id) {
        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isPresent()) {
            Journey journey = optionalJourney.get();
            journey.setConfermato(true);
            return journeyRepository.save(journey);
        } else {
            throw new RuntimeException("Journey non trovato con id " + id);
        }
    }

    // Metodo per aggiornare un Journey esistente
    public Journey updateJourney(Long id, Journey journeyDetails) {
        return journeyRepository.findById(id).map(journey -> {
            journey.setNome(journeyDetails.getNome());
            journey.setDescrizione(journeyDetails.getDescrizione());
            journey.setCreator(journeyDetails.getCreator());
            // L'aggiornamento non modifica automaticamente lo stato "confermato"
            return journeyRepository.save(journey);
        }).orElseThrow(() -> new RuntimeException("Journey non trovato con id: " + id));
    }
}
