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

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
