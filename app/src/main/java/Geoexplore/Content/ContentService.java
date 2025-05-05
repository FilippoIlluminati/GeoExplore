package Geoexplore.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;

@Service
public class ContentService {

    @Autowired private ContentRepository contentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private POIRepository poiRepository;

    public Content createContent(Content content, Long poiId, Long creatorId) {
        Users creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // solo Turista Autenticato e Contributor Autorizzato possono creare
        if (!(creator.getRuolo() == UserRole.TURISTA_AUTENTICATO ||
                creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO)) {
            throw new RuntimeException("Permessi insufficienti per creare contenuti");
        }

        content.setCreator(creator);
        content.setDataCreazione(LocalDateTime.now());

        // contributor autorizzato → APPROVATO subito; turista → IN_ATTESA
        if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO
                && content.getContentType() != ContentType.CONTEST) {
            content.setStatus(ContentStatus.APPROVATO);
        } else {
            content.setStatus(ContentStatus.IN_ATTESA);
        }

        // gestione POI associate (GENERIC / POI)
        if (content.getContentType() == ContentType.POI) {
            if (poiId == null) {
                throw new IllegalArgumentException("poiId richiesto per contentType POI");
            }
            POI poi = poiRepository.findById(poiId)
                    .orElseThrow(() -> new RuntimeException("POI non trovato"));
            content.setPoi(poi);
        } else {
            content.setPoi(null);
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

    public Content updateStatus(Long contentId, ContentStatus status) {
        Content c = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato"));
        c.setStatus(status);
        return contentRepository.save(c);
    }
}
