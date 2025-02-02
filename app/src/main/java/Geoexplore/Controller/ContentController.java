package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contents")
public class ContentController {

    @Autowired
    private ContentService contentService;

    // Crea un nuovo contenuto
    @PostMapping
    public ResponseEntity<?> createContent(@RequestBody Content content) {
        try {
            Content savedContent = contentService.createContent(content);
            return ResponseEntity.ok(savedContent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        Optional<Content> contentOpt = contentService.getContentById(id);
        return contentOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna un contenuto esistente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContent(@PathVariable Long id, @RequestBody Content content) {
        try {
            Content updatedContent = contentService.updateContent(id, content);
            return ResponseEntity.ok(updatedContent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Elimina un contenuto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    // Recupera i contenuti approvati
    @GetMapping("/approved")
    public ResponseEntity<List<Content>> getApprovedContents() {
        List<Content> approvedContents = contentService.getApprovedContents();
        return ResponseEntity.ok(approvedContents);
    }

    // Recupera i contenuti in attesa di approvazione
    @GetMapping("/pending")
    public ResponseEntity<List<Content>> getPendingContents() {
        List<Content> pendingContents = contentService.getPendingContents();
        return ResponseEntity.ok(pendingContents);
    }

    // Approva un contenuto: endpoint /contents/{contentId}/approve/{approverId}
    @PutMapping("/{contentId}/approve/{approverId}")
    public ResponseEntity<?> approveContent(@PathVariable Long contentId, @PathVariable Long approverId) {
        try {
            Content approvedContent = contentService.approveContent(contentId, approverId);
            return ResponseEntity.ok(approvedContent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
