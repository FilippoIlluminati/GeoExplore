package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import Geoexplore.Content.ContentStatus;
import Geoexplore.Content.ContentType;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired private ContentService contentService;
    @Autowired private UserRepository  userRepository;

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
    public ResponseEntity<?> getPendingContents(
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
        if (user.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(403).body("Solo un curatore può visualizzare i contenuti in attesa.");
        }
        return ResponseEntity.ok(contentService.getPendingContents());
    }

    @PostMapping("/create")
    public ResponseEntity<Content> createContent(
            @RequestBody Content content,
            @RequestParam(required = false) Long poiId,
            @AuthenticationPrincipal UserDetails principal) {

        Users creator = userRepository.findByUsername(principal.getUsername());
        if (creator == null) {
            return ResponseEntity.status(404).build();
        }

        // Creo il contenuto (di default in ATTESA)
        Content saved = contentService.createContent(content, poiId, creator.getId());

        // Se è CURATORE o CONTRIBUTOR_AUTORIZZATO, lo approvo subito
        if (creator.getRuolo() == UserRole.CURATORE ||
                creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO) {
            saved = contentService.updateStatus(saved.getId(), ContentStatus.APPROVATO);
        }

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateContent(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody Content updatedContent) {

        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
        if (user.getRuolo() != UserRole.CONTRIBUTOR_AUTORIZZATO) {
            return ResponseEntity.status(403).body("Solo un contributor autorizzato può aggiornare contenuti.");
        }
        Optional<Content> maybe = contentService.getRawContentById(id);
        if (maybe.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Content existing = maybe.get();
        if (!existing.getCreator().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Puoi modificare solo i contenuti creati da te.");
        }
        if (existing.getContentType() == ContentType.CONTEST) {
            return ResponseEntity.status(403).body("Non è possibile modificare contenuti di tipo CONTEST.");
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
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
        if (user.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(403).body("Solo un curatore può approvare contenuti.");
        }
        return ResponseEntity.ok(contentService.updateStatus(id, ContentStatus.APPROVATO));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectContent(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body("Utente non trovato");
        }
        if (user.getRuolo() != UserRole.CURATORE) {
            return ResponseEntity.status(403).body("Solo un curatore può rifiutare contenuti.");
        }
        return ResponseEntity.ok(contentService.updateStatus(id, ContentStatus.RIFIUTATO));
    }
}
