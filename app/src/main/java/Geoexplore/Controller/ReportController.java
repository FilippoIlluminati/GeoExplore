package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIRepository;
import Geoexplore.Report.Report;
import Geoexplore.Report.ReportManager;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    /**
     * Segnala un POI tramite URL rapido:
     * POST /reports/{poiId}
     * - se l'utente è autenticato, ne incamera le credenziali come reporter
     */
    @PostMapping("/{poiId}")
    public ResponseEntity<Report> quickReportByPath(
            @PathVariable Long poiId,
            Authentication authentication
    ) {
        POI poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI non trovato"));

        // Costruisco il Report
        Report report = new Report();
        report.setTipo("SEGNALAZIONE");
        report.setDescrizione("Segnalazione rapida POI #" + poiId);
        report.setPoi(poi);

        // Se arriva Authentication valida e principal è un Users, lo registro come reporter
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Users) {
                report.setReporter((Users) principal);
            }
        }

        Report saved = reportManager.saveReport(report);
        return ResponseEntity.ok(saved);
    }

    /**
     * Creazione “classica” via JSON body:
     * POST /reports
     */
    @PostMapping
    public ResponseEntity<Report> createReport(
            @RequestBody Report report,
            Authentication authentication
    ) {
        // Se l’utente è autenticato, lo registro come reporter
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Users) {
                report.setReporter((Users) principal);
            }
        }
        Report saved = reportManager.saveReport(report);
        return ResponseEntity.ok(saved);
    }

    /**
     * Recupera tutti i report
     */
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportManager.getAllReports());
    }

    /**
     * Recupera un report per ID
     */
    @GetMapping("/{id}/view")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Optional<Report> rep = reportManager.getReportById(id);
        return rep.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Elimina un report per ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        if (reportManager.getReportById(id).isPresent()) {
            reportManager.deleteReport(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
