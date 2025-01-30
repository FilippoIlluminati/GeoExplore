package Geoexplore.POI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class POIManager {

    private final POIRepository poiRepository;

    @Autowired
    public POIManager(POIRepository poiRepository) {
        this.poiRepository = poiRepository;
    }

    public POI savePOI(POI poi) {
        return poiRepository.save(poi);
    }

    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    public Optional<POI> getPOIById(Long id) {
        return poiRepository.findById(id);
    }

    public void deletePOI(Long id) {
        poiRepository.deleteById(id);
    }
}
