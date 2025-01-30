package Geoexplore.Journey;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {

    @Autowired
    private JourneyRepository journeyRepository;

    // Ottiene tutti i viaggi
    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    // Trova un viaggio per ID
    public Optional<Journey> getJourneyById(Long id) {
        return journeyRepository.findById(id);
    }

    // Salva un nuovo viaggio
    public Journey createJourney(Journey journey) {
        return journeyRepository.save(journey);
    }

    // Aggiorna un viaggio esistente
    public Journey updateJourney(Long id, Journey journeyDetails) {
        return journeyRepository.findById(id).map(journey -> {
            journey.setNome(journeyDetails.getNome());
            journey.setDescrizione(journeyDetails.getDescrizione());
            journey.setCreator(journeyDetails.getCreator());
            return journeyRepository.save(journey);
        }).orElseThrow(() -> new RuntimeException("Journey not found"));
    }

    // Elimina un viaggio
    public void deleteJourney(Long id) {
        journeyRepository.deleteById(id);
    }
}