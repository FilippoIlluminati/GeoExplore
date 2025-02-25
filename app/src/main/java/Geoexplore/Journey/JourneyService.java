package Geoexplore.Journey;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository; // Assicurati che esista un repository per i POI
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
    private POIRepository poiRepository; // Per caricare i POI reali dal DB

    // Helper per ottenere l'utente autenticato
    private Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utente non autenticato");
        }
        return user;
    }

    // Crea un nuovo journey
    public Journey createJourney(Journey journey) {
        // Il journey deve contenere almeno 2 POI
        if (journey.getPoiList() == null || journey.getPoiList().size() < 2) {
            throw new RuntimeException("Un journey deve contenere almeno 2 POI");
        }

        // Carichiamo dal DB i POI effettivi, così da avere i campi reali (nome, descrizione, ecc.)
        List<POI> realPoiList = new ArrayList<>();
        for (POI partialPoi : journey.getPoiList()) {
            POI dbPoi = poiRepository.findById(partialPoi.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "POI non trovato con id " + partialPoi.getId()));
            realPoiList.add(dbPoi);
        }
        journey.setPoiList(realPoiList);

        // Verifica che il creator sia specificato e recuperabile dal DB
        if (journey.getCreator() == null || journey.getCreator().getId() == null) {
            throw new RuntimeException("Creator non specificato");
        }
        Optional<Users> optionalCreator = userRepository.findById(journey.getCreator().getId());
        if (optionalCreator.isEmpty()) {
            throw new RuntimeException("Creator non trovato");
        }
        Users creator = optionalCreator.get();
        journey.setCreator(creator);

        // Solo CONTRIBUTOR e CONTRIBUTOR_AUTORIZZATO possono creare un journey
        if (creator.getRuolo() != UserRole.CONTRIBUTOR && creator.getRuolo() != UserRole.CONTRIBUTOR_AUTORIZZATO) {
            throw new RuntimeException("Solo i contributor possono creare un journey");
        }

        // Imposta lo stato di conferma in base al ruolo:
        if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO) {
            journey.setConfermato(true);
        } else {
            journey.setConfermato(false);
        }

        return journeyRepository.save(journey);
    }

    // Recupera tutti i journey
    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    // Trova un journey per ID
    public Optional<Journey> getJourneyById(Long id) {
        return journeyRepository.findById(id);
    }

    // Aggiorna un journey esistente:
    // Solo il creatore può modificarlo.
    public Journey updateJourney(Long id, Journey journeyDetails) {
        Users currentUser = getAuthenticatedUser();
        return journeyRepository.findById(id).map(journey -> {
            // Controllo creatore
            if (!journey.getCreator().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Solo il creatore del journey può modificarlo");
            }
            // Controllo approvazione
            if (journey.isConfermato()) {
                throw new RuntimeException("Il journey già approvato non può essere modificato");
            }
            // Controllo POI
            if (journeyDetails.getPoiList() == null || journeyDetails.getPoiList().size() < 2) {
                throw new RuntimeException("Un journey deve contenere almeno 2 POI");
            }

            // Carichiamo i POI reali dal DB, così i campi del POI risultano valorizzati
            List<POI> realPoiList = new ArrayList<>();
            for (POI partialPoi : journeyDetails.getPoiList()) {
                POI dbPoi = poiRepository.findById(partialPoi.getId())
                        .orElseThrow(() -> new RuntimeException(
                                "POI non trovato con id " + partialPoi.getId()));
                realPoiList.add(dbPoi);
            }

            // Applichiamo le modifiche
            journey.setNome(journeyDetails.getNome());
            journey.setDescrizione(journeyDetails.getDescrizione());
            journey.setOrdinato(journeyDetails.isOrdinato());
            journey.setPoiList(realPoiList);

            return journeyRepository.save(journey);
        }).orElseThrow(() -> new RuntimeException("Journey non trovato con id: " + id));
    }

    // Elimina un journey:
    // Solo il creatore può eliminarlo.
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

    // Approvazione del journey:
    // Solo il curatore (CURATORE) può approvare un journey.
    public Journey approveJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new RuntimeException("Solo il gestore della piattaforma può approvare i POI");
        }
        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isEmpty()) {
            throw new RuntimeException("Journey non trovato con id " + id);
        }
        Journey journey = optionalJourney.get();
        journey.setConfermato(true);
        return journeyRepository.save(journey);
    }

    // Rifiuto del journey:
    // Solo il curatore (CURATORE) può rifiutare un journey; in tal caso il journey viene eliminato.
    public void rejectJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            throw new RuntimeException("Solo il gestore della piattaforma può rifiutare i POI");
        }
        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isEmpty()) {
            throw new RuntimeException("Journey non trovato con id " + id);
        }
        journeyRepository.deleteById(id);
    }
}
