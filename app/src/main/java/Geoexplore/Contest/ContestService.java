package Geoexplore.Contest;

import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContestService {

    @Autowired private ContestRepository contestRepository;
    @Autowired private ContestEntryRepository contestEntryRepository;
    @Autowired private UserRepository userRepository;

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

    public ContestEntry partecipaAlConcorso(Long concorsoId,
                                            ContestEntry partecipazione,
                                            Long partecipanteId) {
        Contest concorso = contestRepository.findById(concorsoId)
                .orElseThrow(() -> new RuntimeException("Concorso non trovato."));
        if (concorso.isInvitazionale() &&
                (concorso.getInvitedUserIds() == null ||
                        !concorso.getInvitedUserIds().contains(partecipanteId))) {
            throw new SecurityException("Utente non invitato.");
        }
        Users utente = userRepository.findById(partecipanteId)
                .orElseThrow(() -> new RuntimeException("Partecipante non trovato."));
        if (partecipazione == null) partecipazione = new ContestEntry();
        partecipazione.setConcorso(concorso);
        partecipazione.setPartecipante(utente);
        partecipazione.setStato(StatoPartecipazione.IN_ATTESA);
        if (partecipazione.getContenuto() == null) {
            partecipazione.setContenuto("");
        }
        return contestEntryRepository.save(partecipazione);
    }

    public ContestEntry submitContent(Long partecipazioneId,
                                      String contenuto,
                                      Long partecipanteId) {
        ContestEntry entry = contestEntryRepository.findById(partecipazioneId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata."));
        if (!entry.getPartecipante().getId().equals(partecipanteId)) {
            throw new SecurityException("Solo il partecipante può inviare il contenuto.");
        }
        entry.setContenuto(contenuto);
        entry.setStato(StatoPartecipazione.IN_ATTESA);
        return contestEntryRepository.save(entry);
    }

    public ContestEntry approvaPartecipazione(Long partecipazioneId, Long validatoreId) {
        Users validatore = userRepository.findById(validatoreId)
                .orElseThrow(() -> new RuntimeException("Validatore non trovato."));
        if (!(validatore.getRuolo() == UserRole.ANIMATORE || validatore.getRuolo() == UserRole.CURATORE)) {
            throw new SecurityException("Solo Animatore o Curatore possono approvare.");
        }
        ContestEntry entry = contestEntryRepository.findById(partecipazioneId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata."));
        if (entry.getStato() != StatoPartecipazione.IN_ATTESA) {
            throw new IllegalStateException("Stato non valido per approvazione.");
        }
        entry.setStato(StatoPartecipazione.APPROVATA);
        return contestEntryRepository.save(entry);
    }

    public ContestEntry rifiutaPartecipazione(Long partecipazioneId, Long validatoreId) {
        Users validatore = userRepository.findById(validatoreId)
                .orElseThrow(() -> new RuntimeException("Validatore non trovato."));
        if (!(validatore.getRuolo() == UserRole.ANIMATORE || validatore.getRuolo() == UserRole.CURATORE)) {
            throw new SecurityException("Solo Animatore o Curatore possono rifiutare.");
        }
        ContestEntry entry = contestEntryRepository.findById(partecipazioneId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata."));
        if (entry.getStato() != StatoPartecipazione.IN_ATTESA) {
            throw new IllegalStateException("Stato non valido per rifiuto.");
        }
        entry.setStato(StatoPartecipazione.RIFIUTATA);
        return contestEntryRepository.save(entry);
    }

    public List<ContestEntry> getPartecipazioniPerConcorso(Long concorsoId) {
        return contestEntryRepository.findByConcorsoId(concorsoId);
    }

    public List<Contest> getAllConcorsi() {
        return contestRepository.findAll();
    }

    public List<Contest> getConcorsiByStato(StatoConcorso stato) {
        return contestRepository.findByStato(stato);
    }

    public Contest getContestById(Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concorso non trovato."));
    }

    public List<ContestEntry> getPartecipazioniPerAnimatore(Long animatoreId) {
        List<Contest> concorsi = getAllConcorsi().stream()
                .filter(c -> c.getCreatore() != null && c.getCreatore().getId().equals(animatoreId))
                .collect(Collectors.toList());

        List<ContestEntry> all = new ArrayList<>();
        concorsi.forEach(c -> {
            if (c.getPartecipazioni() != null) {
                all.addAll(c.getPartecipazioni());
            }
        });

        return all;
    }
}
