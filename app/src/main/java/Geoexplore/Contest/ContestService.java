package Geoexplore.Contest;

import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserRepository userRepository;

    // Ottiene tutti i contest
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    // Trova un contest per ID
    public Optional<Contest> getContestById(Long id) {
        return contestRepository.findById(id);
    }

    // Crea un nuovo contest
    public Contest createContest(Contest contest) {
        Long orgId = contest.getOrganizzatore().getId();
        Users organizzatore = userRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organizzatore non trovato con id: " + orgId));
        // Solo un animatore o il gestore della piattaforma possono creare contest
        if (!(organizzatore.getRuolo().equals(UserRole.ANIMATORE) ||
                organizzatore.getRuolo().equals(UserRole.GESTORE_PIATTAFORMA))) {
            throw new RuntimeException("Solo un animatore o il gestore della piattaforma possono creare contest");
        }
        contest.setOrganizzatore(organizzatore);
        return contestRepository.save(contest);
    }

    // Aggiorna un contest esistente
    public Contest updateContest(Long id, Contest contestDetails) {
        return contestRepository.findById(id).map(contest -> {
            contest.setTitolo(contestDetails.getTitolo());
            contest.setObiettivo(contestDetails.getObiettivo());
            contest.setDescrizione(contestDetails.getDescrizione());
            contest.setDataInizio(contestDetails.getDataInizio());
            contest.setDataFine(contestDetails.getDataFine());
            contest.setOrganizzatore(contestDetails.getOrganizzatore());
            contest.setStatus(contestDetails.getStatus());
            return contestRepository.save(contest);
        }).orElseThrow(() -> new RuntimeException("Contest non trovato"));
    }

    // Aggiorna lo stato di un contest
    public Contest updateContestStatus(Long id, ContestStatus newStatus) {
        return contestRepository.findById(id).map(contest -> {
            contest.setStatus(newStatus);
            return contestRepository.save(contest);
        }).orElseThrow(() -> new RuntimeException("Contest non trovato con ID: " + id));
    }

    // Registra un partecipante a un contest
    public Contest registerParticipant(Long contestId, Long userId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest non trovato con ID: " + contestId));
        // Verifica che il contest sia aperto
        if (contest.getStatus() != ContestStatus.APERTO) {
            throw new RuntimeException("Il contest non è aperto per le iscrizioni");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));
        // Solo i contributor autorizzati possono partecipare
        if (!user.getRuolo().equals(UserRole.CONTRIBUTOR_AUTORIZZATO)) {
            throw new RuntimeException("Solo i contributor autorizzati possono partecipare al contest");
        }
        contest.getPartecipanti().add(user);
        return contestRepository.save(contest);
    }

    // Elimina un contest
    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }
}
