package Geoexplore.Controller;

import Geoexplore.Contest.*;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import Geoexplore.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private UserRepository userRepository;

    // Endpoint per creare un concorso (solo da parte di un Animatore)
    @PostMapping("/create")
    public ResponseEntity<?> creaConcorso(@RequestBody Contest concorso, @RequestParam Long creatoreId) {
        Users creatore = userRepository.findById(creatoreId)
                .orElseThrow(() -> new RuntimeException("Creatore non trovato"));
        if (creatore.getRuolo() != UserRole.ANIMATORE) {
            return ResponseEntity.status(403).body("Solo un Animatore può creare un concorso.");
        }
        Contest concorsoCreato = contestService.creaConcorso(concorso, creatoreId);
        return ResponseEntity.ok("Concorso creato con successo. ID: " + concorsoCreato.getId());
    }

    // Endpoint per partecipare ad un concorso (per Contributor o Contributor Autorizzato)
    @PostMapping("/{concorsoId}/join")
    public ResponseEntity<?> partecipaAlConcorso(@PathVariable Long concorsoId,
                                                 @RequestParam Long partecipanteId,
                                                 @RequestBody(required = false) ContestEntry partecipazione) {
        if (partecipazione == null) {
            partecipazione = new ContestEntry();
            partecipazione.setContenuto("");
        }
        ContestEntry partecipazioneCreato = contestService.partecipaAlConcorso(concorsoId, partecipazione, partecipanteId);
        return ResponseEntity.ok("Partecipazione inviata. ID partecipazione: " + partecipazioneCreato.getId());
    }

    // Endpoint per visualizzare tutte le partecipazioni di un concorso
    @GetMapping("/{concorsoId}/partecipazioni")
    public ResponseEntity<?> getPartecipazioni(@PathVariable Long concorsoId) {
        List<ContestEntry> partecipazioni = contestService.getPartecipazioniPerConcorso(concorsoId);
        return ResponseEntity.ok(partecipazioni);
    }

    // Endpoint per approvare una partecipazione (solo da parte di un Animatore o Curatore)
    @PatchMapping("/partecipazione/{partecipazioneId}/approva")
    public ResponseEntity<?> approvaPartecipazione(@PathVariable Long partecipazioneId, @RequestParam Long validatoreId) {
        try {
            ContestEntry partecipazione = contestService.approvaPartecipazione(partecipazioneId, validatoreId);
            return ResponseEntity.ok("Partecipazione approvata. ID: " + partecipazione.getId());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Errore nell'approvazione: " + ex.getMessage());
        }
    }

    // Endpoint per rifiutare una partecipazione (solo da parte di un Animatore o Curatore)
    @PatchMapping("/partecipazione/{partecipazioneId}/rifiuta")
    public ResponseEntity<?> rifiutaPartecipazione(@PathVariable Long partecipazioneId, @RequestParam Long validatoreId) {
        try {
            ContestEntry partecipazione = contestService.rifiutaPartecipazione(partecipazioneId, validatoreId);
            return ResponseEntity.ok("Partecipazione rifiutata. ID: " + partecipazione.getId());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Errore nel rifiuto: " + ex.getMessage());
        }
    }

    // Endpoint per recuperare tutti i concorsi (con le relative partecipazioni, grazie a fetch EAGER)
    @GetMapping("/all")
    public ResponseEntity<?> getAllConcorsi() {
        return ResponseEntity.ok(contestService.getAllConcorsi());
    }

    // Endpoint per recuperare concorsi filtrati per stato
    @GetMapping("/status")
    public ResponseEntity<?> getConcorsiByStato(@RequestParam String stato) {
        StatoConcorso s;
        try {
            s = StatoConcorso.valueOf(stato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Stato non valido. I possibili valori sono: BOZZA, IN_CORSO, CHIUSO.");
        }
        return ResponseEntity.ok(contestService.getConcorsiByStato(s));
    }

    // Endpoint per recuperare un concorso per ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getContestById(@PathVariable Long id) {
        Contest contest = contestService.getContestById(id);
        return ResponseEntity.ok(contest);
    }

    // Nuovo endpoint: Recupera tutte le partecipazioni per i concorsi creati da un Animatore
    @GetMapping("/animator/{animatoreId}/partecipazioni")
    public ResponseEntity<?> getPartecipazioniByAnimatore(@PathVariable Long animatoreId) {
        List<ContestEntry> partecipazioni = contestService.getPartecipazioniPerAnimatore(animatoreId);
        return ResponseEntity.ok(partecipazioni);
    }
}
