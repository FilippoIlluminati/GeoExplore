package Geoexplore.Journey;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class StopService {

    @Autowired
    private StopRepository stopRepository;

    // Ottiene tutti i punti di sosta
    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    // Trova un punto di sosta per ID
    public Optional<Stop> getStopById(Long id) {
        return stopRepository.findById(id);
    }

    // Salva un nuovo punto di sosta
    public Stop createStop(Stop stop) {
        return stopRepository.save(stop);
    }

    // Aggiorna un punto di sosta esistente
    public Stop updateStop(Long id, Stop stopDetails) {
        return stopRepository.findById(id).map(stop -> {
            stop.setNome(stopDetails.getNome());
            stop.setDescrizione(stopDetails.getDescrizione());
            stop.setCategoria(stopDetails.getCategoria());
            stop.setCoordinate(stopDetails.getCoordinate());
            stop.setJourney(stopDetails.getJourney());
            return stopRepository.save(stop);
        }).orElseThrow(() -> new RuntimeException("Stop not found"));
    }

    // Elimina un punto di sosta
    public void deleteStop(Long id) {
        stopRepository.deleteById(id);
    }
}