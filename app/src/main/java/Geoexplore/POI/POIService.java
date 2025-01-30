package Geoexplore.POI;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    // Ottiene tutti i punti di interesse
    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    // Trova un punto di interesse per ID
    public Optional<POI> getPOIById(Long id) {
        return poiRepository.findById(id);
    }

    // Salva un nuovo punto di interesse
    public POI createPOI(POI poi) {
        return poiRepository.save(poi);
    }

    // Aggiorna un punto di interesse esistente
    public POI updatePOI(Long id, POI poiDetails) {
        return poiRepository.findById(id).map(poi -> {
            poi.setNome(poiDetails.getNome());
            poi.setDescrizione(poiDetails.getDescrizione());
            poi.setCategoria(poiDetails.getCategoria());
            poi.setCoordinate(poiDetails.getCoordinate());
            return poiRepository.save(poi);
        }).orElseThrow(() -> new RuntimeException("POI not found"));
    }

    // Elimina un punto di interesse
    public void deletePOI(Long id) {
        poiRepository.deleteById(id);
    }
}
