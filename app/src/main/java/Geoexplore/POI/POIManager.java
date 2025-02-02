package Geoexplore.POI;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Geoexplore.User.UserRole;
import Geoexplore.User.Users;

@Component
public class POIManager {

    @Autowired
    private POIService poiService;

    // Recupera tutti i POI
    public List<POI> getAllPOIs() {
        return poiService.getAllPOIs();
    }

    // Recupera un POI specifico tramite ID
    public Optional<POI> getPOIById(Long id) {
        return poiService.getPOIById(id);
    }

    // Aggiunge un nuovo POI con gestione permessi
    public POI addPOI(POI poi, Long userId) {
        return poiService.addPOI(poi, userId);
    }

    // Aggiorna un POI esistente con controllo permessi
    public POI updatePOI(Long id, POI poi, Long userId) {
        return poiService.updatePOI(id, poi, userId);
    }

    // Elimina un POI con controllo permessi
    public void deletePOI(Long id, Long userId) {
        poiService.deletePOI(id, userId);
    }

    // Recupera POI non approvati
    public List<POI> getUnapprovedPOIs() {
        return poiService.getUnapprovedPOIs();
    }

    // Approva un POI con controllo permessi
    public POI approvePOI(Long id, Long userId) {
        return poiService.approvePOI(id, userId);
    }
}