package Geoexplore.Report;

import Geoexplore.Journey.Stop;
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
    @JoinColumn(name = "stopID", nullable = false)
    private Stop stop;

    @ManyToOne
    @JoinColumn(name = "reporterID", nullable = false)
    private Users reporter;

    // Costruttore vuoto richiesto da JPA
    public Report() {}

    // Costruttore con parametri
    public Report(String tipo, String descrizione, Stop stop, Users reporter) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.stop = stop;
        this.reporter = reporter;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public Users getReporter() {
        return reporter;
    }

    public void setReporter(Users reporter) {
        this.reporter = reporter;
    }
}