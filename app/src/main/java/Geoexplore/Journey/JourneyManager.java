package Geoexplore.Journey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JourneyManager {

    private final JourneyRepository journeyRepository;

    @Autowired
    public JourneyManager(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    public Journey saveJourney(Journey journey) {
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
}
