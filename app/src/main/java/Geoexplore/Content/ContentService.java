package Geoexplore.Content;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    // Crea un nuovo contenuto associato a un POI; il creatorId identifica l'utente che crea il contenuto
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

        // Se il creatore è CONTRIBUTOR o TURISTA_AUTENTICATO, il contenuto va in attesa di approvazione.
        // Se il creatore è CONTRIBUTOR_AUTORIZZATO o CURATORE, il contenuto viene salvato già come approvato.
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

    public List<Content> getContentsByPOI(Long poiId) {
        return contentRepository.findByPoiId(poiId);
    }

    public Content approveContent(Long contentId, Long validatorId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato."));

        Users validator = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Validator non trovato."));

        if (!(validator.getRuolo() == UserRole.CURATORE || validator.getRuolo() == UserRole.ANIMATORE)) {
            throw new SecurityException("Solo Curatore o Animatore possono approvare i contenuti.");
        }

        if (content.getStatus() != ContentStatus.IN_ATTESA) {
            throw new IllegalStateException("Content non in stato IN_ATTESA.");
        }

        content.setStatus(ContentStatus.APPROVATO);
        return contentRepository.save(content);
    }

    public Content rejectContent(Long contentId, Long validatorId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato."));

        Users validator = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Validator non trovato."));

        if (!(validator.getRuolo() == UserRole.CURATORE || validator.getRuolo() == UserRole.ANIMATORE)) {
            throw new SecurityException("Solo Curatore o Animatore possono rifiutare i contenuti.");
        }

        if (content.getStatus() != ContentStatus.IN_ATTESA) {
            throw new IllegalStateException("Content non in stato IN_ATTESA.");
        }

        content.setStatus(ContentStatus.RIFIUTATO);
        return contentRepository.save(content);
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
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
