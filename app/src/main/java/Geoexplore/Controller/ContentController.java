package Geoexplore.Controller;

import Geoexplore.Content.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    // Crea un nuovo contenuto associato a un POI
    // Parametri: poiId e creatorId per identificare il POI e chi crea il contenuto
    @PostMapping("/create")
    public ResponseEntity<?> createContent(
            @RequestBody Content content,
            @RequestParam Long poiId,
            @RequestParam Long creatorId) {

        Content saved = contentService.createContent(content, poiId, creatorId);
        return ResponseEntity.ok("Content creato con successo. ID: " + saved.getId() +
                " Stato: " + saved.getStatus());
    }

    // Ottiene i contenuti associati a un POI
    @GetMapping("/poi/{poiId}")
    public ResponseEntity<?> getContentsByPOI(@PathVariable Long poiId) {
        List<Content> contents = contentService.getContentsByPOI(poiId);
        return ResponseEntity.ok(contents);
    }

    // Approva un contenuto (validatorId deve essere di un utente con ruolo CURATORE o ANIMATORE)
    @PatchMapping("/{contentId}/approve")
    public ResponseEntity<?> approveContent(
            @PathVariable Long contentId,
            @RequestParam Long validatorId) {

        Content updated = contentService.approveContent(contentId, validatorId);
        return ResponseEntity.ok("Content approvato. ID: " + updated.getId());
    }

    // Rifiuta un contenuto
    @PatchMapping("/{contentId}/reject")
    public ResponseEntity<?> rejectContent(
            @PathVariable Long contentId,
            @RequestParam Long validatorId) {

        Content updated = contentService.rejectContent(contentId, validatorId);
        return ResponseEntity.ok("Content rifiutato. ID: " + updated.getId());
    }

    // Ottiene tutti i contenuti
    @GetMapping("/all")
    public ResponseEntity<?> getAllContents() {
        return ResponseEntity.ok(contentService.getAllContents());
    }
}
