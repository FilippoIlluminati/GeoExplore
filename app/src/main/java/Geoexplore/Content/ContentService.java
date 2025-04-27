// src/main/java/Geoexplore/Content/ContentService.java
package Geoexplore.Content;

import Geoexplore.POI.POIRepository;
import Geoexplore.POI.POI;
import Geoexplore.Contest.Contest;
import Geoexplore.Contest.ContestEntry;
import Geoexplore.Contest.ContestEntryRepository;
import Geoexplore.Contest.ContestRepository;
import Geoexplore.Contest.StatoPartecipazione;
import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContentService {

    @Autowired private ContentRepository contentRepository;
    @Autowired private POIRepository poiRepository;
    @Autowired private ContestRepository contestRepository;
    @Autowired private ContestEntryRepository entryRepository;
    @Autowired private UserRepository userRepository;

    // — Logica invariata per POI/GENERIC —

    public Content createContent(Content content, Long poiId, Long creatorId) {
        POI poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI non trovato."));
        Users creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator non trovato."));
        if (!puoCreareContenuti(creator.getRuolo())) {
            throw new SecurityException("Il ruolo " + creator.getRuolo() + " non può creare contenuti.");
        }
        content.setPoi(poi);
        content.setDataCreazione(LocalDateTime.now());
        switch (creator.getRuolo()) {
            case TURISTA_AUTENTICATO:
            case CONTRIBUTOR:
                content.setStatus(ContentStatus.IN_ATTESA);
                break;
            case CONTRIBUTOR_AUTORIZZATO:
            case CURATORE:
                content.setStatus(ContentStatus.APPROVATO);
                break;
            default:
                content.setStatus(ContentStatus.IN_ATTESA);
        }
        return contentRepository.save(content);
    }

    // — Nuova logica per CONTEST —

    public Content createContestContent(Content content,
                                        Long contestId,
                                        Long entryId,
                                        Long creatorId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest non trovato."));
        ContestEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata."));
        if (!entry.getConcorso().getId().equals(contestId)) {
            throw new SecurityException("Partecipazione non appartiene a questo contest.");
        }
        if (entry.getStato() != StatoPartecipazione.APPROVATA) {
            throw new IllegalStateException("Partecipazione non approvata.");
        }
        if (!entry.getPartecipante().getId().equals(creatorId)) {
            throw new SecurityException("Solo il partecipante può inviare il contenuto.");
        }

        content.setContest(contest);
        content.setContentType(ContentType.CONTEST);
        content.setDataCreazione(LocalDateTime.now());
        content.setStatus(ContentStatus.IN_ATTESA);
        return contentRepository.save(content);
    }

    public List<Content> getContentsByPOI(Long poiId) {
        return contentRepository.findByPoiId(poiId);
    }

    public List<Content> getContentsByContest(Long contestId) {
        return contentRepository.findByContestId(contestId);
    }

    public Content approveContestContent(Long contentId, Long validatorId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato."));
        if (content.getContentType() != ContentType.CONTEST) {
            throw new IllegalStateException("Non è un contest content.");
        }
        Users validator = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Validatore non trovato."));
        if (!(validator.getRuolo() == UserRole.ANIMATORE ||
                validator.getRuolo() == UserRole.CURATORE)) {
            throw new SecurityException("Solo Animatore o Curatore possono approvare.");
        }
        if (content.getStatus() != ContentStatus.IN_ATTESA) {
            throw new IllegalStateException("Content non in stato IN_ATTESA.");
        }
        content.setStatus(ContentStatus.APPROVATO);
        return contentRepository.save(content);
    }

    public Content rejectContestContent(Long contentId, Long validatorId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato."));
        if (content.getContentType() != ContentType.CONTEST) {
            throw new IllegalStateException("Non è un contest content.");
        }
        Users validator = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Validatore non trovato."));
        if (!(validator.getRuolo() == UserRole.ANIMATORE ||
                validator.getRuolo() == UserRole.CURATORE)) {
            throw new SecurityException("Solo Animatore o Curatore possono rifiutare.");
        }
        if (content.getStatus() != ContentStatus.IN_ATTESA) {
            throw new IllegalStateException("Content non in stato IN_ATTESA.");
        }
        content.setStatus(ContentStatus.RIFIUTATO);
        return contentRepository.save(content);
    }

    private boolean puoCreareContenuti(UserRole ruolo) {
        switch (ruolo) {
            case TURISTA_AUTENTICATO:
            case CONTRIBUTOR:
            case CONTRIBUTOR_AUTORIZZATO:
            case CURATORE:
            case ANIMATORE:
                return true;
            default:
                return false;
        }
    }
}
