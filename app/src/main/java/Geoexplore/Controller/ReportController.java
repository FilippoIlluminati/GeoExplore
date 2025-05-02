package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.Content.Content;
import Geoexplore.Content.ContentRepository;
import Geoexplore.Content.ContentStatus;
import Geoexplore.Report.Report;
import Geoexplore.Report.ReportManager;
import Geoexplore.Report.ReportStatus;
import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportManager reportManager;

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    /** 1) Segnalazione rapida di un POI */
    @PostMapping("/{poiId}")
    public ResponseEntity<Report> quickReport(@PathVariable Long poiId) {
        POI poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI non trovato"));

        Report rpt = new Report();
        rpt.setTipo("POI");
        rpt.setDescrizione("Segnalazione rapida POI #" + poiId);
        rpt.setPoi(poi);
        rpt.setStato(ReportStatus.IN_ATTESA);

        return ResponseEntity.ok(reportManager.saveReport(rpt));
    }

    /** 2) Creazione “classica” via JSON body */
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        report.setStato(ReportStatus.IN_ATTESA);
        return ResponseEntity.ok(reportManager.saveReport(report));
    }

    /** 3) Recupera tutti i report */
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportManager.getAllReports());
    }

    /** 4) Recupera un report per ID */
    @GetMapping("/{id}/view")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Optional<Report> maybe = reportManager.getReportById(id);
        return maybe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** 5) Ignora la segnalazione (solo Gestore) */
    @DeleteMapping("/{id}/ignore")
    public ResponseEntity<Void> ignoreReport(
            @PathVariable Long id,
            @RequestParam Long managerId
    ) {
        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trovato"));
        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA)
            return ResponseEntity.status(403).build();

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Report non trovato"));
        rpt.setStato(ReportStatus.IGNORATO);
        reportManager.saveReport(rpt);
        return ResponseEntity.noContent().build();
    }

    /** 6) Elimina l’oggetto segnalato (POI o Content) + la segnalazione (solo Gestore) */
    @DeleteMapping("/{id}/resolve-delete")
    public ResponseEntity<Void> resolveAndDelete(
            @PathVariable Long id,
            @RequestParam Long managerId
    ) {
        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trovato"));
        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA)
            return ResponseEntity.status(403).build();

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Report non trovato"));

        // elimina fisicamente l’oggetto
        if ("POI".equals(rpt.getTipo())) {
            poiRepository.deleteById(rpt.getPoi().getId());
        } else if ("CONTENT".equals(rpt.getTipo())) {
            contentRepository.deleteById(rpt.getContent().getId());
        } else {
            throw new RuntimeException("Tipo di segnalazione non supportato: " + rpt.getTipo());
        }

        // poi elimina la segnalazione
        reportManager.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    /** 7) “Respinge” l’oggetto segnalato ma lo tiene, marca report come RISOLTO */
    @PatchMapping("/{id}/resolve-reject")
    public ResponseEntity<Void> resolveAndReject(
            @PathVariable Long id,
            @RequestParam Long managerId
    ) {
        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trovato"));
        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA)
            return ResponseEntity.status(403).build();

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Report non trovato"));

        // modifica lo stato dell’oggetto senza cancellarlo
        if ("POI".equals(rpt.getTipo())) {
            POI poi = rpt.getPoi();
            poi.setApprovato(false);
            poiRepository.save(poi);
        } else if ("CONTENT".equals(rpt.getTipo())) {
            Content content = rpt.getContent();
            content.setStatus(ContentStatus.RIFIUTATO);
            contentRepository.save(content);
        } else {
            throw new RuntimeException("Tipo di segnalazione non supportato: " + rpt.getTipo());
        }

        // marca report come risolto
        rpt.setStato(ReportStatus.RISOLTO);
        reportManager.saveReport(rpt);
        return ResponseEntity.noContent().build();
    }

}
