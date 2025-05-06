package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import Geoexplore.Content.ContentStatus;
import Geoexplore.Content.ContentType;      // <— import mancante
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(contentService.getApprovedContents());
    }

    @GetMapping("/poi/{poiId}")
    public ResponseEntity<List<Content>> getContentsByPoi(@PathVariable Long poiId) {
        return ResponseEntity.ok(contentService.getApprovedContentsByPoi(poiId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Optional<Content> content = contentService.getContentById(id);
        return content.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingContents(@RequestParam Long validatorId) {
        Users user = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (user.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(403).body("Solo un curatore può visualizzare i contenuti in attesa.");
        }
        return ResponseEntity.ok(contentService.getPendingContents());
    }

    @PostMapping("/create")
    public ResponseEntity<Content> createContent(
            @RequestBody Content content,
            @RequestParam(required = false) Long poiId,
            @RequestParam Long creatorId
    ) {
        Content saved = contentService.createContent(content, poiId, creatorId);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateContent(
            @PathVariable Long id,
            @RequestParam Long contributorId,
            @RequestBody Content updatedContent
    ) {
        Users user = userRepository.findById(contributorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (user.getRuolo() != UserRole.CONTRIBUTOR_AUTORIZZATO) {
            return ResponseEntity.status(403).body("Solo un contributor autorizzato può aggiornare contenuti.");
        }

        Optional<Content> maybeContent = contentService.getRawContentById(id);
        if (maybeContent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Content existing = maybeContent.get();

        if (!existing.getCreator().getId().equals(contributorId)) {
            return ResponseEntity.status(403).body("Puoi modificare solo i contenuti creati da te.");
        }

        // Blocca solo i contenuti di tipo CONTEST
        if (existing.getContentType() == ContentType.CONTEST) {
            return ResponseEntity.status(403).body("Non è possibile modificare contenuti legati a un contest.");
        }

        existing.setTitolo(updatedContent.getTitolo());
        existing.setDescrizione(updatedContent.getDescrizione());
        existing.setMultimediaUrl(updatedContent.getMultimediaUrl());

        Content saved = contentService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveContent(
            @PathVariable Long id,
            @RequestParam Long validatorId
    ) {
        Users user = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (user.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(403).body("Solo un curatore può approvare contenuti.");
        }
        Content approved = contentService.updateStatus(id, ContentStatus.APPROVATO);
        return ResponseEntity.ok(approved);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectContent(
            @PathVariable Long id,
            @RequestParam Long validatorId
    ) {
        Users user = userRepository.findById(validatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (user.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(403).body("Solo un curatore può rifiutare contenuti.");
        }
        Content rejected = contentService.updateStatus(id, ContentStatus.RIFIUTATO);
        return ResponseEntity.ok(rejected);
    }
}
