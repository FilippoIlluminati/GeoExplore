package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired private ContentService contentService;

    @PostMapping("/create")
    public ResponseEntity<?> createContent(
            @RequestBody Content content,
            @RequestParam Long poiId,
            @RequestParam Long creatorId) {
        try {
            Content saved = contentService.createContent(content, poiId, creatorId);
            return ResponseEntity.ok("Content creato. ID: " + saved.getId() +
                    " Stato: " + saved.getStatus());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/poi/{poiId}")
    public ResponseEntity<List<Content>> getContentsByPOI(@PathVariable Long poiId) {
        return ResponseEntity.ok(contentService.getContentsByPOI(poiId));
    }

    @PatchMapping("/{contentId}/approve")
    public ResponseEntity<?> approveContent(
            @PathVariable Long contentId,
            @RequestParam Long validatorId) {
        try {
            Content upd = contentService.approveContent(contentId, validatorId);
            return ResponseEntity.ok("Content approvato. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/{contentId}/reject")
    public ResponseEntity<?> rejectContent(
            @PathVariable Long contentId,
            @RequestParam Long validatorId) {
        try {
            Content upd = contentService.rejectContent(contentId, validatorId);
            return ResponseEntity.ok("Content rifiutato. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/contest/create")
    public ResponseEntity<?> createContestContent(
            @RequestBody Content content,
            @RequestParam Long contestId,
            @RequestParam Long entryId,
            @RequestParam Long creatorId) {
        try {
            Content saved = contentService.createContestContent(
                    content, contestId, entryId, creatorId);
            return ResponseEntity.ok("Contest Content creato. ID: "
                    + saved.getId() +
                    " Stato: " + saved.getStatus());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/contest/{contestId}")
    public ResponseEntity<List<Content>> getContestContents(@PathVariable Long contestId) {
        return ResponseEntity.ok(contentService.getContentsByContest(contestId));
    }

    @PatchMapping("/contest/{contentId}/approve")
    public ResponseEntity<?> approveContestContent(
            @PathVariable Long contentId,
            @RequestParam Long validatorId) {
        try {
            Content upd = contentService.approveContestContent(contentId, validatorId);
            return ResponseEntity.ok("Contest Content approvato. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/contest/{contentId}/reject")
    public ResponseEntity<?> rejectContestContent(
            @PathVariable Long contentId,
            @RequestParam Long validatorId) {
        try {
            Content upd = contentService.rejectContestContent(contentId, validatorId);
            return ResponseEntity.ok("Contest Content rifiutato. ID: " + upd.getId());
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
