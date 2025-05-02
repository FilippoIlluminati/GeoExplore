package Geoexplore.Report;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import Geoexplore.POI.POI;
import Geoexplore.Content.Content;
import Geoexplore.User.Users;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // tipo di segnalazione: "POI" oppure "CONTENT"
    private String tipo;

    @Column(length = 2048)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    private POI poi;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Users reporter;

    private LocalDateTime dataInvio = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ReportStatus stato = ReportStatus.IN_ATTESA;

    public Report() {}

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Users getReporter() {
        return reporter;
    }

    public void setReporter(Users reporter) {
        this.reporter = reporter;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }

    public ReportStatus getStato() {
        return stato;
    }

    public void setStato(ReportStatus stato) {
        this.stato = stato;
    }
}
