package Geoexplore.POI;

import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    private static final double CENTER_LAT = 43.2482194;
    private static final double CENTER_LON = 13.5075306;
    private static final double ALLOWED_RADIUS_METERS = 750;

    private boolean isWithinAllowedArea(double lat, double lon) {
        double distance = calculateDistance(CENTER_LAT, CENTER_LON, lat, lon);
        return distance <= ALLOWED_RADIUS_METERS;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utente non trovato o non autenticato");
        }
        return user;
    }

    public POI createPOI(POI poi) {
        if (!isWithinAllowedArea(poi.getLatitude(), poi.getLongitude())) {
            throw new RuntimeException("Impossibile creare il POI: fuori dal limite di competenza");
        }

        if (poi.getCreator() == null || poi.getCreator().getId() == null) {
            throw new RuntimeException("Creazione POI fallita: creatore non specificato");
        }

        Users creator = userRepository.findById(poi.getCreator().getId())
                .orElseThrow(() -> new RuntimeException("Creatore non trovato"));

        poi.setCreator(creator);

        if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO) {
            poi.setApprovato(true);
        } else if (creator.getRuolo() == UserRole.CONTRIBUTOR) {
            poi.setApprovato(false);
        } else {
            throw new RuntimeException("Non hai il permesso di creare un POI");
        }

        return poiRepository.save(poi);
    }

    public POI updatePOI(Long id, POI updatedPOI) {
        Users currentUser = getAuthenticatedUser();
        POI existingPOI = poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("POI non trovato con id " + id));

        if (!existingPOI.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Solo il creatore del POI può modificarlo");
        }

        if (!isWithinAllowedArea(updatedPOI.getLatitude(), updatedPOI.getLongitude())) {
            throw new RuntimeException("Impossibile aggiornare il POI: fuori dal limite di competenza");
        }

        existingPOI.setNome(updatedPOI.getNome());
        existingPOI.setDescrizione(updatedPOI.getDescrizione());
        existingPOI.setLatitude(updatedPOI.getLatitude());
        existingPOI.setLongitude(updatedPOI.getLongitude());
        existingPOI.setCategoria(updatedPOI.getCategoria());
        existingPOI.setComune(updatedPOI.getComune());

        return poiRepository.save(existingPOI);
    }

    public void deletePOI(Long id) {
        Users currentUser = getAuthenticatedUser();
        POI poi = poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("POI non trovato con id " + id));

        if (!poi.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Solo il creatore del POI può eliminarlo");
        }

        poiRepository.deleteById(id);
    }

    public Optional<POI> getPOIById(Long id) {
        return poiRepository.findById(id);
    }

    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    public POI approvePOI(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new RuntimeException("Solo il gestore della piattaforma può approvare i POI");
        }

        POI poi = poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("POI non trovato con id " + id));

        poi.setApprovato(true);
        return poiRepository.save(poi);
    }

    public void rejectPOI(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new RuntimeException("Solo il gestore della piattaforma può rifiutare i POI");
        }

        POI poi = poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("POI non trovato con id " + id));

        poiRepository.deleteById(id);
    }

    public Users savePOIForTurista(Long poiId) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.TURISTA_AUTENTICATO) {
            throw new RuntimeException("Solo i Turista Autenticati possono salvare POI per visite future");
        }

        POI poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI non trovato con id " + poiId));

        currentUser.getSavedPois().add(poi);
        return userRepository.save(currentUser);
    }

    public Set<POI> getSavedPOIsForTurista() {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.TURISTA_AUTENTICATO) {
            throw new RuntimeException("Solo i Turista Autenticati possono avere POI salvati");
        }
        return currentUser.getSavedPois();
    }
}
