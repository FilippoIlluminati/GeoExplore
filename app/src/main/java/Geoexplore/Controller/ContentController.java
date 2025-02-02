package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    // **CRUD DI BASE**

    // Crea un nuovo contenuto
    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody Content content) {
        Content savedContent = contentService.createContent(content);
        return ResponseEntity.ok(savedContent);
    }

    // Recupera tutti i contenuti
    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        List<Content> contents = contentService.getAllContents();
        return ResponseEntity.ok(contents);
    }

    // Recupera un contenuto per ID
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Optional<Content> content = contentService.getContentById(id);
        return content.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un contenuto per ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTENT')")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        if (contentService.getContentById(id).isPresent()) {
            contentService.deleteContent(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // **NUOVI ENDPOINT PER APPROVAZIONE**

    // Recupera solo i contenuti approvati
    @GetMapping("/approved")
    public ResponseEntity<List<Content>> getApprovedContents() {
        List<Content> contents = contentService.getApprovedContents();
        return ResponseEntity.ok(contents);
    }

    // Recupera solo i contenuti in attesa di approvazione
    @GetMapping("/pending")
    public ResponseEntity<List<Content>> getPendingContents() {
        List<Content> contents = contentService.getPendingContents();
        return ResponseEntity.ok(contents);
    }

    // Approva un contenuto (solo CURATORE o GESTORE_PIATTAFORMA)
    @PutMapping("/{id}/approve/{approverId}")
    @PreAuthorize("hasAnyAuthority('VALIDATE_CONTENT', 'MANAGE_CONTENT')")
    public ResponseEntity<Content> approveContent(@PathVariable Long id, @PathVariable Long approverId) {
        Content approvedContent = contentService.approveContent(id, approverId);
        return ResponseEntity.ok(approvedContent);
    }
}
