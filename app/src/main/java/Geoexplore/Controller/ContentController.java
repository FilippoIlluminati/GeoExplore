package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contents")
public class ContentController {

    private final ContentManager contentManager;

    @Autowired
    public ContentController(ContentManager contentManager) {
        this.contentManager = contentManager;
    }

    // Crea un nuovo contenuto
    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody Content content) {
        Content savedContent = contentManager.saveContent(content);
        return ResponseEntity.ok(savedContent);
    }

    // Recupera tutti i contenuti
    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        List<Content> contents = contentManager.getAllContents();
        return ResponseEntity.ok(contents);
    }

    // Recupera un contenuto per ID
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Optional<Content> content = contentManager.getContentById(id);
        return content.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un contenuto per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        if (contentManager.getContentById(id).isPresent()) {
            contentManager.deleteContent(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
