package Geoexplore.POI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class POIManager {

    @Autowired
    private POIService poiService;

    // Crea un nuovo POI
    public POI createPOI(POI poi) {
        return poiService.createPOI(poi);
    }

    // Aggiorna un POI esistente
    public POI updatePOI(Long id, POI poi) {
        return poiService.updatePOI(id, poi);
    }

    // Elimina un POI per ID
    public void deletePOI(Long id) {
        poiService.deletePOI(id);
    }

    // Restituisce un POI per ID
    public Optional<POI> getPOIById(Long id) {
        return poiService.getPOIById(id);
    }

    // Restituisce tutti i POI
    public List<POI> getAllPOIs() {
        return poiService.getAllPOIs();
    }
}
