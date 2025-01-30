package Geoexplore.Report;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    // Ottiene tutti i report
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Trova un report per ID
    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    // Salva un nuovo report
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    // Aggiorna un report esistente
    public Report updateReport(Long id, Report reportDetails) {
        return reportRepository.findById(id).map(report -> {
            report.setTipo(reportDetails.getTipo());
            report.setDescrizione(reportDetails.getDescrizione());
            report.setStop(reportDetails.getStop());
            report.setReporter(reportDetails.getReporter());
            return reportRepository.save(report);
        }).orElseThrow(() -> new RuntimeException("Report not found"));
    }

    // Elimina un report
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}