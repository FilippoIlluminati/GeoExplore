package Geoexplore.Contest;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    // Ottiene tutti i contest
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    // Trova un contest per ID
    public Optional<Contest> getContestById(Long id) {
        return contestRepository.findById(id);
    }

    // Salva un nuovo contest
    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }

    // Aggiorna un contest esistente
    public Contest updateContest(Long id, Contest contestDetails) {
        return contestRepository.findById(id).map(contest -> {
            contest.setTitolo(contestDetails.getTitolo());
            contest.setDescrizione(contestDetails.getDescrizione());
            contest.setDataInizio(contestDetails.getDataInizio());
            contest.setDataFine(contestDetails.getDataFine());
            contest.setOrganizzatore(contestDetails.getOrganizzatore());
            return contestRepository.save(contest);
        }).orElseThrow(() -> new RuntimeException("Contest not found"));
    }

    // Elimina un contest
    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }
}