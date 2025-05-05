package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
import Geoexplore.Content.ContentStatus;
import Geoexplore.Report.Report;
import Geoexplore.Report.ReportManager;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired private ContentService contentService;
    @Autowired private UserRepository userRepository;
    @Autowired private ReportManager reportManager;

    // 1) GET di tutti i contenuti APPROVATI (accessibile a chiunque)
    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(contentService.getApprovedContents());
    }

    // 2) GET di tutti i contenuti APPROVATI per uno specifico POI
    @GetMapping("/poi/{poiId}")
    public ResponseEntity<List<Content>> getContentsByPoi(@PathVariable Long poiId) {
        return ResponseEntity.ok(contentService.getApprovedContentsByPoi(poiId));
    }

    // 3) GET di un singolo contenuto APPROVATO
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        return contentService.getContentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4) Creazione contenuto (Turista Autenticato o Contributor Autorizzato)
    @PostMapping("/create")
    public ResponseEntity<Content> createContent(
            @RequestParam Long creatorId,
            @RequestParam(required = false) Long poiId,
            @RequestBody Content content) {
        Content created = contentService.createContent(content, poiId, creatorId);
        return ResponseEntity.ok(created);
    }

    // 5) Approva un contenuto (solo Animatore)
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Content> approveContent(
            @PathVariable Long id,
            @RequestParam Long animatorId) {
        Users animator = userRepository.findById(animatorId)
                .orElseThrow(() -> new RuntimeException("Animatore non trovato"));
        if (animator.getRuolo() != UserRole.ANIMATORE) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(contentService.updateStatus(id, ContentStatus.APPROVATO));
    }

    // 6) Rifiuta un contenuto (solo Animatore)
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Content> rejectContent(
            @PathVariable Long id,
            @RequestParam Long animatorId) {
        Users animator = userRepository.findById(animatorId)
                .orElseThrow(() -> new RuntimeException("Animatore non trovato"));
        if (animator.getRuolo() != UserRole.ANIMATORE) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(contentService.updateStatus(id, ContentStatus.RIFIUTATO));
    }

    // 7) Segnala un contenuto (invariato)
    @PostMapping("/{contentId}/report")
    public ResponseEntity<Report> reportContent(
            @PathVariable Long contentId,
            @RequestParam(required = false) Long reporterId,
            @RequestBody(required = false) String descrizione) {
        Content c = contentService.getContentById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato"));

        Report rpt = new Report();
        rpt.setTipo("CONTENT");
        rpt.setDescrizione(
                descrizione != null
                        ? descrizione
                        : "Segnalazione rapida Content #" + contentId
        );
        rpt.setContent(c);
        if (reporterId != null) {
            Users u = userRepository.findById(reporterId)
                    .orElseThrow(() -> new RuntimeException("Reporter non trovato"));
            rpt.setReporter(u);
        }
        rpt.setStato(Geoexplore.Report.ReportStatus.IN_ATTESA);
        return ResponseEntity.ok(reportManager.saveReport(rpt));
    }
}
