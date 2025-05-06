package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import Geoexplore.Content.ContentStatus;
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

    // Restituisce tutti i contenuti con stato APPROVATO
    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(contentService.getApprovedContents());
    }

    // Restituisce i contenuti APPROVATI relativi a un POI specifico
    @GetMapping("/poi/{poiId}")
    public ResponseEntity<List<Content>> getContentsByPoi(@PathVariable Long poiId) {
        return ResponseEntity.ok(contentService.getApprovedContentsByPoi(poiId));
    }

    // Restituisce un singolo contenuto APPROVATO, se presente
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Optional<Content> content = contentService.getContentById(id);
        return content.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Crea un nuovo contenuto (solo per utenti autenticati abilitati)
    @PostMapping("/create")
    public ResponseEntity<Content> createContent(
            @RequestBody Content content,
            @RequestParam(required = false) Long poiId,
            @RequestParam Long creatorId
    ) {
        Content saved = contentService.createContent(content, poiId, creatorId);
        return ResponseEntity.ok(saved);
    }

    // Approvazione di un contenuto (accessibile solo ad utenti ANIMATORE)
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Content> approveContent(
            @PathVariable Long id,
            @RequestParam Long animatorId
    ) {
        Users user = userRepository.findById(animatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (user.getRuolo() != UserRole.ANIMATORE) {
            return ResponseEntity.status(403).body(null);
        }
        Content approved = contentService.updateStatus(id, ContentStatus.APPROVATO);
        return ResponseEntity.ok(approved);
    }

    // Rifiuto di un contenuto (accessibile solo ad utenti ANIMATORE)
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Content> rejectContent(
            @PathVariable Long id,
            @RequestParam Long animatorId
    ) {
        Users user = userRepository.findById(animatorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (user.getRuolo() != UserRole.ANIMATORE) {
            return ResponseEntity.status(403).body(null);
        }
        Content rejected = contentService.updateStatus(id, ContentStatus.RIFIUTATO);
        return ResponseEntity.ok(rejected);
    }
}
