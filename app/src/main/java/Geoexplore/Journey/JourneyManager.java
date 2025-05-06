package Geoexplore.Journey;

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
public class JourneyManager {

    private final JourneyRepository journeyRepository;
    private final UserRepository userRepository;

    @Autowired
    public JourneyManager(JourneyRepository journeyRepository, UserRepository userRepository) {
        this.journeyRepository = journeyRepository;
        this.userRepository = userRepository;
    }

    private Users getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utente non autenticato");
        }
        return user;
    }

    public Journey saveJourney(Journey journey) {
        if (journey.getPoiList() == null || journey.getPoiList().size() < 2) {
            throw new RuntimeException("Un journey deve contenere almeno 2 POI");
        }

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

        if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO) {
            journey.setConfermato(true);
        } else {
            journey.setConfermato(false);
        }

        return journeyRepository.save(journey);
    }

    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    public Optional<Journey> getJourneyById(Long id) {
        return journeyRepository.findById(id);
    }

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

    public Journey updateJourney(Long id, Journey journeyDetails) {
        Users currentUser = getAuthenticatedUser();
        return journeyRepository.findById(id).map(journey -> {
            if (!journey.getCreator().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Solo il creatore del journey può modificarlo");
            }
            if (journey.isConfermato()) {
                throw new RuntimeException("Il journey già approvato non può essere modificato");
            }

            journey.setNome(journeyDetails.getNome());
            journey.setDescrizione(journeyDetails.getDescrizione());
            journey.setOrdinato(journeyDetails.isOrdinato());
            journey.setPoiList(journeyDetails.getPoiList());
            return journeyRepository.save(journey);
        }).orElseThrow(() -> new RuntimeException("Journey non trovato con id: " + id));
    }

    public Journey approveJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.CURATORE) {
            throw new RuntimeException("Solo il curatore può approvare i journey");
        }

        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isPresent()) {
            Journey journey = optionalJourney.get();
            journey.setConfermato(true);
            return journeyRepository.save(journey);
        } else {
            throw new RuntimeException("Journey non trovato con id " + id);
        }
    }

    public void rejectJourney(Long id) {
        Users currentUser = getAuthenticatedUser();
        if (currentUser.getRuolo() != UserRole.CURATORE) {
            throw new RuntimeException("Solo il curatore può rifiutare i journey");
        }

        Optional<Journey> optionalJourney = journeyRepository.findById(id);
        if (optionalJourney.isEmpty()) {
            throw new RuntimeException("Journey non trovato con id " + id);
        }

        journeyRepository.deleteById(id);
    }
}
