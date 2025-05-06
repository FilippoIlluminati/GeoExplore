package Geoexplore.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportManager {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportManager(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // Salva una nuova segnalazione o aggiorna una esistente
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    // Restituisce tutte le segnalazioni presenti nel sistema
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Recupera una segnalazione per ID
    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    // Elimina una segnalazione per ID
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
