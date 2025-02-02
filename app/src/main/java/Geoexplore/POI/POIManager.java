package Geoexplore.POI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class POIManager {

    @Autowired
    private POIService poiService;

    public POI createPOI(POI poi) {
        return poiService.createPOI(poi);
    }

    public POI updatePOI(Long id, POI poi) {
        return poiService.updatePOI(id, poi);
    }

    public void deletePOI(Long id) {
        poiService.deletePOI(id);
    }

    public Optional<POI> getPOIById(Long id) {
        return poiService.getPOIById(id);
    }

    public List<POI> getAllPOIs() {
        return poiService.getAllPOIs();
    }
}
