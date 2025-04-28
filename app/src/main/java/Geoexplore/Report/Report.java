package Geoexplore.Report;

import Geoexplore.POI.POI;
import Geoexplore.User.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "Report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "poiID", nullable = false)
    private POI poi;

    @ManyToOne
    @JoinColumn(name = "reporterID", nullable = true)
    private Users reporter;

    public Report() {}

    public Report(String tipo, String descrizione, POI poi, Users reporter) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.poi = poi;
        this.reporter = reporter;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public POI getPoi() { return poi; }
    public void setPoi(POI poi) { this.poi = poi; }
    public Users getReporter() { return reporter; }
    public void setReporter(Users reporter) { this.reporter = reporter; }
}
