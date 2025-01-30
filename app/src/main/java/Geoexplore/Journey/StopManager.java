package Geoexplore.Journey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StopManager {

    private final StopRepository stopRepository;

    @Autowired
    public StopManager(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public Stop saveStop(Stop stop) {
        return stopRepository.save(stop);
    }

    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    public Optional<Stop> getStopById(Long id) {
        return stopRepository.findById(id);
    }

    public void deleteStop(Long id) {
        stopRepository.deleteById(id);
    }
}
