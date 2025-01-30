package Geoexplore.Controller;

import Geoexplore.Report.Report;
import Geoexplore.Report.ReportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
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
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
