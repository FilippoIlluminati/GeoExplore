package Geoexplore.Journey;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private POIRepository poiRepository;

    // Restituisce l'utente attualmente autenticato
    private Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utente non autenticato");
        }
        return user;
    }

    // Crea un nuovo journey (solo contributor)
    public Journey createJourney(Journey journey) {
        if (journey.getPoiList() == null || journey.getPoiList().size() < 2) {
            throw new RuntimeException("Un journey deve contenere almeno 2 POI");
        }

        List<POI> realPoiList = new ArrayList<>();
        for (POI partialPoi : journey.getPoiList()) {
            POI dbPoi = poiRepository.findById(partialPoi.getId())
                    .orElseThrow(() -> new RuntimeException("POI non trovato con id " + partialPoi.getId()));
            realPoiList.add(dbPoi);
        }
        journey.setPoiList(realPoiList);

        if (journey.getCreator() == null || journey.getCreator().getId() == null) {
            throw new RuntimeException("Creator non specificato");
        }
        Optional<Users> optionalCreator = userRepository.findById(journey.getCreator().getId());
        if (optionalCreator.isEmpty()) {
            throw new RuntimeException("Creator non trovato");
        }
        Users creator = optionalCreator.get();
        journey.setCreator(creator);

        if (creator.getRuolo() != UserRole.CONTRIBUTOR && creator.getRuolo() != UserRole.CONTRIBUTOR_AUTORIZZATO) {
            throw new RuntimeException("Solo i contributor possono creare un journey");
        }

        journey.setConfermato(creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO);

        return journeyRepository.save(journey);
    }

    // Restituisce tutti i journey
    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    // Restituisce un journey specifico per ID
    public Optional<Journey> getJourneyById(Long id) {
        return journeyRepository.findById(id);
    }

    // Aggiorna un journey (solo il creatore e solo se non confermato)
    public Journey updateJourney(Long id, Journey journeyDetails) {
        Users currentUser = getAuthenticatedUser();
        return journeyRepository.findById(id).map(journey -> {
            if (!journey.getCreator().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Solo il creatore del journey può modificarlo");
            }
            if (journey.isConfermato()) {
                throw new RuntimeException("Il journey già approvato non può essere modificato");
            }
            if (journeyDetails.getPoiList() == null || journeyDetails.getPoiList().size() < 2) {
                throw new RuntimeException("Un journey deve contenere almeno 2 POI");
            }

            List<POI> realPoiList = new ArrayList<>();
            for (POI partialPoi : journeyDetails.getPoiList()) {
                POI dbPoi = poiRepository.findById(partialPoi.getId())
                        .orElseThrow(() -> new RuntimeException("POI non trovato con id " + partialPoi.getId()));
                realPoiList.add(dbPoi);
            }

            journey.setNome(journeyDetails.getNome());
            journey.setDescrizione(journeyDetails.getDescrizione());
            journey.setOrdinato(journeyDetails.isOrdinato());
            journey.setPoiList(realPoiList);

            return journeyRepository.save(journey);
        }).orElseThrow(() -> new RuntimeException("Journey non trovato con id: " + id));
    }

    // Elimina un journey (solo il creatore)
    public void deleteJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isEmpty()) {
            throw new RuntimeException("Journey non trovato");
        }
        Journey journey = optionalJourney.get();
        if (!journey.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Solo il creatore del journey può eliminarlo");
        }
        journeyRepository.deleteById(id);
    }

    // Approvazione del journey (solo GESTORE_PIATTAFORMA)
    public Journey approveJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new RuntimeException("Solo il gestore della piattaforma può approvare i journey");
        }
        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isEmpty()) {
            throw new RuntimeException("Journey non trovato con id " + id);
        }
        Journey journey = optionalJourney.get();
        journey.setConfermato(true);
        return journeyRepository.save(journey);
    }

    // Rifiuto del journey (solo GESTORE_PIATTAFORMA)
    public void rejectJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new RuntimeException("Solo il gestore della piattaforma può rifiutare i journey");
        }
        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isEmpty()) {
            throw new RuntimeException("Journey non trovato con id " + id);
        }
        journeyRepository.deleteById(id);
    }
}
