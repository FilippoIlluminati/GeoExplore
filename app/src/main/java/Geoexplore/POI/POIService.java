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

@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    // Coordinate del centro di Corridonia (come in map.html)
    private static final double CENTER_LAT = 43.2482194;
    private static final double CENTER_LON = 13.5075306;
    // Raggio consentito in metri
    private static final double ALLOWED_RADIUS_METERS = 750;

    // Metodo per verificare se le coordinate sono nel range consentito
    private boolean isWithinAllowedArea(double lat, double lon) {
        double distance = calculateDistance(CENTER_LAT, CENTER_LON, lat, lon);
        return distance <= ALLOWED_RADIUS_METERS;
    }

    // Calcola la distanza in metri tra due coordinate (formula Haversine)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Raggio della Terra in metri
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Helper per ottenere l'utente autenticato corrente
    private Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utente non trovato o non autenticato");
        }
        return user;
    }

    // Creazione del POI (solo CONTRIBUTOR e CONTRIBUTOR_AUTORIZZATO possono creare POI)
    public POI createPOI(POI poi) {
        // Verifica che le coordinate siano nel range consentito
        if (!isWithinAllowedArea(poi.getLatitude(), poi.getLongitude())) {
            throw new RuntimeException("Impossibile creare il POI: fuori dal limite di competenza");
        }
        // Il creatore deve essere specificato
        if (poi.getCreator() == null || poi.getCreator().getId() == null) {
            throw new RuntimeException("Creazione POI fallita: creatore non specificato");
        }
        Optional<Users> creatorOpt = userRepository.findById(poi.getCreator().getId());
        if (!creatorOpt.isPresent()) {
            throw new RuntimeException("Creatore non trovato");
        }
        Users creator = creatorOpt.get();
        poi.setCreator(creator);
        // Solo CONTRIBUTOR e CONTRIBUTOR_AUTORIZZATO hanno il permesso di creare POI
        if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO) {
            poi.setApprovato(true);
        } else if (creator.getRuolo() == UserRole.CONTRIBUTOR) {
            poi.setApprovato(false);
        } else {
            throw new RuntimeException("Non hai il permesso di creare un POI");
        }
        return poiRepository.save(poi);
    }

    // Aggiornamento del POI: può essere aggiornato solo dal creatore del POI
    public POI updatePOI(Long id, POI updatedPOI) {
        Users currentUser = getAuthenticatedUser();
        Optional<POI> optionalPOI = poiRepository.findById(id);
        if (optionalPOI.isEmpty()) {
            throw new RuntimeException("POI non trovato con id " + id);
        }
        POI existingPOI = optionalPOI.get();
        // Controllo: solo il creatore può aggiornare il POI
        if (!existingPOI.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Solo il creatore del POI può modificarlo");
        }
        // Verifica che le nuove coordinate siano nel range consentito
        if (!isWithinAllowedArea(updatedPOI.getLatitude(), updatedPOI.getLongitude())) {
            throw new RuntimeException("Impossibile aggiornare il POI: fuori dal limite di competenza");
        }
        // Aggiorna i campi del POI (il creatore non viene modificato)
        existingPOI.setNome(updatedPOI.getNome());
        existingPOI.setDescrizione(updatedPOI.getDescrizione());
        existingPOI.setLatitude(updatedPOI.getLatitude());
        existingPOI.setLongitude(updatedPOI.getLongitude());
        existingPOI.setCategoria(updatedPOI.getCategoria());
        existingPOI.setComune(updatedPOI.getComune());
        return poiRepository.save(existingPOI);
    }

    // Eliminazione del POI: può essere eliminato solo dal creatore del POI
    public void deletePOI(Long id) {
        Users currentUser = getAuthenticatedUser();
        Optional<POI> optionalPOI = poiRepository.findById(id);
        if (optionalPOI.isEmpty()) {
            throw new RuntimeException("POI non trovato con id " + id);
        }
        POI poi = optionalPOI.get();
        // Controllo: solo il creatore può eliminare il POI
        if (!poi.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Solo il creatore del POI può eliminarlo");
        }
        poiRepository.deleteById(id);
    }

    // Recupera un POI per ID
    public Optional<POI> getPOIById(Long id) {
        return poiRepository.findById(id);
    }

    // Recupera tutti i POI
    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    // Approvazione del POI: può essere approvato solo dal CURATORE
    public POI approvePOI(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.CURATORE) {
            throw new RuntimeException("Solo il curatore può approvare i POI");
        }
        Optional<POI> optionalPOI = poiRepository.findById(id);
        if (optionalPOI.isPresent()) {
            POI poi = optionalPOI.get();
            poi.setApprovato(true);
            return poiRepository.save(poi);
        } else {
            throw new RuntimeException("POI non trovato con id " + id);
        }
    }

    // Rifiuto del POI: può essere rifiutato solo dal CURATORE; in caso di rifiuto, il POI viene eliminato
    public void rejectPOI(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.CURATORE) {
            throw new RuntimeException("Solo il curatore può rifiutare i POI");
        }
        Optional<POI> optionalPOI = poiRepository.findById(id);
        if (optionalPOI.isEmpty()) {
            throw new RuntimeException("POI non trovato con id " + id);
        }
        poiRepository.deleteById(id);
    }
}
