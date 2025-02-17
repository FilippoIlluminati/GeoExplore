package Geoexplore.Contest;

import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestEntryRepository contestEntryRepository;

    @Autowired
    private UserRepository userRepository;

    // Creazione di un concorso da parte di un Animatore
    public Contest creaConcorso(Contest concorso, Long creatoreId) {
        Users creatore = userRepository.findById(creatoreId)
                .orElseThrow(() -> new RuntimeException("Creatore non trovato."));
        if (creatore.getRuolo() != UserRole.ANIMATORE) {
            throw new SecurityException("Solo un Animatore può creare un concorso.");
        }
        concorso.setCreatore(creatore);
        concorso.setStato(StatoConcorso.IN_CORSO);
        return contestRepository.save(concorso);
    }

    // Partecipazione ad un concorso (il contenuto è opzionale)
    public ContestEntry partecipaAlConcorso(Long concorsoId, ContestEntry partecipazione, Long partecipanteId) {
        Contest concorso = contestRepository.findById(concorsoId)
                .orElseThrow(() -> new RuntimeException("Concorso non trovato."));
        // Se il concorso è invitazionale, controlla che l'utente sia invitato
        if (concorso.isInvitazionale()) {
            List<Long> invitati = concorso.getInvitedUserIds();
            if (invitati == null || !invitati.contains(partecipanteId)) {
                throw new SecurityException("Questo concorso è su invito e l'utente non è invitato.");
            }
        }
        Users partecipante = userRepository.findById(partecipanteId)
                .orElseThrow(() -> new RuntimeException("Partecipante non trovato."));
        // Se il payload è nullo o il contenuto è null, impostalo a stringa vuota
        if (partecipazione == null) {
            partecipazione = new ContestEntry();
        }
        if (partecipazione.getContenuto() == null) {
            partecipazione.setContenuto("");
        }
        partecipazione.setConcorso(concorso);
        partecipazione.setPartecipante(partecipante);
        partecipazione.setStato(StatoPartecipazione.IN_ATTESA);
        return contestEntryRepository.save(partecipazione);
    }

    // Recupera tutte le partecipazioni per un concorso
    public List<ContestEntry> getPartecipazioniPerConcorso(Long concorsoId) {
        return contestEntryRepository.findByConcorsoId(concorsoId);
    }

    // Approvazione di una partecipazione: solo Animatore o Curatore possono approvare
    public ContestEntry approvaPartecipazione(Long partecipazioneId, Long validatoreId) {
        Users validatore = userRepository.findById(validatoreId)
                .orElseThrow(() -> new RuntimeException("Validatore non trovato."));
        if (!(validatore.getRuolo() == UserRole.ANIMATORE || validatore.getRuolo() == UserRole.CURATORE)) {
            throw new SecurityException("Solo Animatore o Curatore possono approvare le partecipazioni.");
        }
        ContestEntry partecipazione = contestEntryRepository.findById(partecipazioneId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata."));
        if (partecipazione.getStato() != StatoPartecipazione.IN_ATTESA) {
            throw new IllegalStateException("La partecipazione non è in stato IN_ATTESA.");
        }
        partecipazione.setStato(StatoPartecipazione.APPROVATA);
        return contestEntryRepository.save(partecipazione);
    }

    // Rifiuto di una partecipazione (stesso controllo)
    public ContestEntry rifiutaPartecipazione(Long partecipazioneId, Long validatoreId) {
        Users validatore = userRepository.findById(validatoreId)
                .orElseThrow(() -> new RuntimeException("Validatore non trovato."));
        if (!(validatore.getRuolo() == UserRole.ANIMATORE || validatore.getRuolo() == UserRole.CURATORE)) {
            throw new SecurityException("Solo Animatore o Curatore possono rifiutare le partecipazioni.");
        }
        ContestEntry partecipazione = contestEntryRepository.findById(partecipazioneId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata."));
        if (partecipazione.getStato() != StatoPartecipazione.IN_ATTESA) {
            throw new IllegalStateException("La partecipazione non è in stato IN_ATTESA.");
        }
        partecipazione.setStato(StatoPartecipazione.RIFIUTATA);
        return contestEntryRepository.save(partecipazione);
    }

    // Recupera tutti i concorsi
    public List<Contest> getAllConcorsi() {
        return contestRepository.findAll();
    }

    // Recupera concorsi filtrati per stato
    public List<Contest> getConcorsiByStato(StatoConcorso stato) {
        return contestRepository.findByStato(stato);
    }
}
