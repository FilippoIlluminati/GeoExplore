package Geoexplore.Content;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired private ContentRepository contentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private POIRepository poiRepository;

    public Content createContent(Content content, Long poiId, Long creatorId) {
        Users creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Ora anche CURATORE può creare contenuti
        if (!(creator.getRuolo() == UserRole.TURISTA_AUTENTICATO ||
                creator.getRuolo() == UserRole.CONTRIBUTOR ||
                creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                creator.getRuolo() == UserRole.CURATORE)) {
            throw new RuntimeException("Permessi insufficienti per creare contenuti");
        }

        content.setCreator(creator);
        content.setDataCreazione(LocalDateTime.now());

        // Associo il POI se richiesto
        if (content.getContentType() == ContentType.POI) {
            if (poiId == null) {
                throw new IllegalArgumentException("ID del POI richiesto per contenuti di tipo POI");
            }
            POI poi = poiRepository.findById(poiId)
                    .orElseThrow(() -> new RuntimeException("POI non trovato"));
            content.setPoi(poi);
        } else {
            content.setPoi(null);
        }

        // Auto-approve per CONTRIBUTOR_AUTORIZZATO e CURATORE (tranne i contest)
        if ((creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                creator.getRuolo() == UserRole.CURATORE)
                && content.getContentType() != ContentType.CONTEST) {
            content.setStatus(ContentStatus.APPROVATO);
        } else {
            content.setStatus(ContentStatus.IN_ATTESA);
        }

        return contentRepository.save(content);
    }

    public List<Content> getApprovedContents() {
        return contentRepository.findByStatus(ContentStatus.APPROVATO);
    }

    public List<Content> getApprovedContentsByPoi(Long poiId) {
        return contentRepository.findByPoiIdAndStatus(poiId, ContentStatus.APPROVATO);
    }

    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id)
                .filter(c -> c.getStatus() == ContentStatus.APPROVATO);
    }

    public Optional<Content> getRawContentById(Long id) {
        return contentRepository.findById(id);
    }

    public Content updateStatus(Long contentId, ContentStatus status) {
        Content c = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenuto non trovato"));
        c.setStatus(status);
        return contentRepository.save(c);
    }

    public List<Content> getPendingContents() {
        return contentRepository.findByStatus(ContentStatus.IN_ATTESA);
    }

    public Content save(Content content) {
        return contentRepository.save(content);
    }
}
