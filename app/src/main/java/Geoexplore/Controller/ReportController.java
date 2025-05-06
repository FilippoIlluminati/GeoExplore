package Geoexplore.Controller;

import Geoexplore.Report.Report;
import Geoexplore.Report.ReportManager;
import Geoexplore.Report.ReportStatus;
import Geoexplore.Content.Content;
import Geoexplore.Content.ContentRepository;
import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import Geoexplore.User.UserRole;
import Geoexplore.Content.ContentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired private ReportManager      reportManager;
    @Autowired private POIRepository      poiRepository;
    @Autowired private ContentRepository  contentRepository;
    @Autowired private UserRepository     userRepository;

    // GET: tutte le segnalazioni (solo Gestore)
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports(
            @AuthenticationPrincipal UserDetails principal) {
        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null || user.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(reportManager.getAllReports());
    }

    // GET: singola segnalazione per ID (solo Gestore)
    @GetMapping("/{id}/view")
    public ResponseEntity<Report> getReportById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null || user.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        Optional<Report> rpt = reportManager.getReportById(id);
        return rpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET: filtra per stato (solo Gestore)
    @GetMapping("/status")
    public ResponseEntity<List<Report>> getByStatus(
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails principal) {
        Users user = userRepository.findByUsername(principal.getUsername());
        if (user == null || user.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        ReportStatus rs;
        try {
            rs = ReportStatus.valueOf(status);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                reportManager.getAllReports()
                        .stream()
                        .filter(r -> r.getStato() == rs)
                        .toList()
        );
    }

    // POST anonimi: segnalazione rapida di un POI
    @PostMapping("/poi/{poiId}")
    public ResponseEntity<Report> quickReportPoi(@PathVariable Long poiId) {
        POI poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI non trovato"));
        Report rpt = new Report();
        rpt.setTipo("POI");
        rpt.setDescrizione("Segnalazione rapida per POI #" + poiId);
        rpt.setPoi(poi);
        rpt.setStato(ReportStatus.IN_ATTESA);
        return ResponseEntity.ok(reportManager.saveReport(rpt));
    }

    // POST anonimi: segnalazione rapida di un Content
    @PostMapping("/content/{contentId}")
    public ResponseEntity<Report> quickReportContent(@PathVariable Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content non trovato"));
        Report rpt = new Report();
        rpt.setTipo("CONTENT");
        rpt.setDescrizione("Segnalazione rapida per Content #" + contentId);
        rpt.setContent(content);
        rpt.setStato(ReportStatus.IN_ATTESA);
        return ResponseEntity.ok(reportManager.saveReport(rpt));
    }

    // POST anonimi: creazione standard di segnalazione via JSON
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        report.setStato(ReportStatus.IN_ATTESA);
        return ResponseEntity.ok(reportManager.saveReport(report));
    }

    // POST anonimi: creazione via query params (type + id)
    @PostMapping(params = {"type", "id"})
    public ResponseEntity<Report> quickReportGeneric(
            @RequestParam("type") String type,
            @RequestParam("id") Long id) {

        Report rpt = new Report();
        rpt.setStato(ReportStatus.IN_ATTESA);

        if ("POI".equalsIgnoreCase(type)) {
            POI poi = poiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("POI non trovato"));
            rpt.setTipo("POI");
            rpt.setDescrizione("Segnalazione rapida per POI #" + id);
            rpt.setPoi(poi);

        } else if ("CONTENT".equalsIgnoreCase(type)) {
            Content content = contentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Content non trovato"));
            rpt.setTipo("CONTENT");
            rpt.setDescrizione("Segnalazione rapida per Content #" + id);
            rpt.setContent(content);

        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reportManager.saveReport(rpt));
    }

    // DELETE ignore (solo Gestore)
    @DeleteMapping("/{id}/ignore")
    public ResponseEntity<Void> ignoreReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        Users mgr = userRepository.findByUsername(principal.getUsername());
        if (mgr == null || mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));
        rpt.setStato(ReportStatus.IGNORATO);
        reportManager.saveReport(rpt);
        return ResponseEntity.noContent().build();
    }

    // DELETE resolve-delete (solo Gestore)
    @DeleteMapping("/{id}/resolve-delete")
    public ResponseEntity<Void> resolveAndDelete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        Users mgr = userRepository.findByUsername(principal.getUsername());
        if (mgr == null || mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));
        switch (rpt.getTipo()) {
            case "POI"     -> poiRepository.deleteById(rpt.getPoi().getId());
            case "CONTENT" -> contentRepository.deleteById(rpt.getContent().getId());
            default        -> throw new RuntimeException("Tipo non supportato");
        }
        reportManager.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH resolve-reject (solo Gestore)
    @PatchMapping("/{id}/resolve-reject")
    public ResponseEntity<Void> resolveAndReject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        Users mgr = userRepository.findByUsername(principal.getUsername());
        if (mgr == null || mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }
        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));
        switch (rpt.getTipo()) {
            case "POI" -> {
                var poi = rpt.getPoi();
                poi.setApprovato(false);
                poiRepository.save(poi);
            }
            case "CONTENT" -> {
                var content = rpt.getContent();
                content.setStatus(ContentStatus.RIFIUTATO);
                contentRepository.save(content);
            }
            default -> throw new RuntimeException("Tipo non supportato");
        }
        rpt.setStato(ReportStatus.RISOLTO);
        reportManager.saveReport(rpt);
        return ResponseEntity.noContent().build();
    }
}
