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

    @Autowired private ReportManager reportManager;
    @Autowired private POIRepository poiRepository;
    @Autowired private ContentRepository contentRepository;
    @Autowired private UserRepository userRepository;

    // 1. Segnalazione rapida di un POI
    @PostMapping("/{poiId}")
    public ResponseEntity<Report> quickReport(@PathVariable Long poiId) {
        POI poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI non trovato"));

        Report rpt = new Report();
        rpt.setTipo("POI");
        rpt.setDescrizione("Segnalazione rapida per POI #" + poiId);
        rpt.setPoi(poi);
        rpt.setStato(ReportStatus.IN_ATTESA);

        return ResponseEntity.ok(reportManager.saveReport(rpt));
    }

    // 2. Creazione standard di una segnalazione via JSON
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        report.setStato(ReportStatus.IN_ATTESA);
        return ResponseEntity.ok(reportManager.saveReport(report));
    }

    // 3. Recupera tutte le segnalazioni
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportManager.getAllReports());
    }

    // 4. Recupera una segnalazione per ID
    @GetMapping("/{id}/view")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Optional<Report> maybe = reportManager.getReportById(id);
        return maybe.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Ignora la segnalazione (solo GESTORE)
    @DeleteMapping("/{id}/ignore")
    public ResponseEntity<Void> ignoreReport(@PathVariable Long id,
                                             @RequestParam Long managerId) {
        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Gestore non trovato"));

        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));

        rpt.setStato(ReportStatus.IGNORATO);
        reportManager.saveReport(rpt);

        return ResponseEntity.noContent().build();
    }

    // 6. Elimina l'oggetto segnalato (POI o Content) e la segnalazione (solo GESTORE)
    @DeleteMapping("/{id}/resolve-delete")
    public ResponseEntity<Void> resolveAndDelete(@PathVariable Long id,
                                                 @RequestParam Long managerId) {
        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Gestore non trovato"));

        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));

        // Elimina l'oggetto associato
        switch (rpt.getTipo()) {
            case "POI" -> poiRepository.deleteById(rpt.getPoi().getId());
            case "CONTENT" -> contentRepository.deleteById(rpt.getContent().getId());
            default -> throw new RuntimeException("Tipo di segnalazione non supportato: " + rpt.getTipo());
        }

        // Elimina la segnalazione
        reportManager.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    // 7. Rifiuta l'oggetto ma lo mantiene (marcando il report come RISOLTO)
    @PatchMapping("/{id}/resolve-reject")
    public ResponseEntity<Void> resolveAndReject(@PathVariable Long id,
                                                 @RequestParam Long managerId) {
        Users mgr = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Gestore non trovato"));

        if (mgr.getRuolo() != UserRole.GESTORE_PIATTAFORMA) {
            return ResponseEntity.status(403).build();
        }

        Report rpt = reportManager.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Segnalazione non trovata"));

        // Aggiorna lo stato dell'oggetto senza eliminarlo
        switch (rpt.getTipo()) {
            case "POI" -> {
                POI poi = rpt.getPoi();
                poi.setApprovato(false);
                poiRepository.save(poi);
            }
            case "CONTENT" -> {
                Content content = rpt.getContent();
                content.setStatus(ContentStatus.RIFIUTATO);
                contentRepository.save(content);
            }
            default -> throw new RuntimeException("Tipo di segnalazione non supportato: " + rpt.getTipo());
        }

        // Marca la segnalazione come risolta
        rpt.setStato(ReportStatus.RISOLTO);
        reportManager.saveReport(rpt);

        return ResponseEntity.noContent().build();
    }
}
