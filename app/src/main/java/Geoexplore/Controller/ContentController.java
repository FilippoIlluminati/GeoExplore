package Geoexplore.Controller;

import Geoexplore.Content.Content;
import Geoexplore.Content.ContentService;
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
    @Autowired private ReportManager reportManager;
    @Autowired private UserRepository userRepository;

    /** 1) Rendo visibile a tutti i contenuti approvati */
    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(contentService.getAllContents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        return contentService.getContentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** 2) Segnalazione di un content (solo autenticati) */
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

    /** 3) Il gestore può IGNORARE il report */
    @DeleteMapping("/reports/{id}/ignore")
    public ResponseEntity<Void> ignoreReport(
            @PathVariable Long id,
            @RequestParam Long managerId) {

        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trovato"));
        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        if (!reportManager.getReportById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // solo ignoro il report (lo lascio nel db con stato IGNORATO)
        reportManager.getReportById(id).ifPresent(r -> {
            r.setStato(Geoexplore.Report.ReportStatus.IGNORATO);
            reportManager.saveReport(r);
        });
        return ResponseEntity.noContent().build();
    }

    /** 4) Il gestore può ELIMINARE il content segnalato + cancellare il report */
    @DeleteMapping("/reports/{id}/resolve-delete")
    public ResponseEntity<Void> resolveAndDeleteReport(
            @PathVariable Long id,
            @RequestParam Long managerId) {

        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trovato"));
        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Report non trovato"));
        // elimino il content
        Long contentId = rpt.getContent().getId();
        contentService.deleteContent(contentId);
        // poi elimino il report stesso
        reportManager.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
