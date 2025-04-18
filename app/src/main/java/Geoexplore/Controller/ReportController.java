package Geoexplore.Controller;

import Geoexplore.Report.Report;
import Geoexplore.Report.ReportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportManager reportManager;

    @Autowired
    public ReportController(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    // Crea un nuovo Report
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report, Authentication authentication) {
        // Se l'utente è autenticato, imposta automaticamente il reporter
        if (authentication != null && authentication.isAuthenticated()) {
            // Supponendo che il principal sia un'istanza di Users
            Object principal = authentication.getPrincipal();
            if (principal instanceof Geoexplore.User.Users) {
                report.setReporter((Geoexplore.User.Users) principal);
            }
        }
        Report savedReport = reportManager.saveReport(report);
        return ResponseEntity.ok(savedReport);
    }

    // Recupera tutti i Report
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportManager.getAllReports();
        return ResponseEntity.ok(reports);
    }

    // Recupera un Report per ID
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Optional<Report> report = reportManager.getReportById(id);
        return report.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un Report per ID
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
