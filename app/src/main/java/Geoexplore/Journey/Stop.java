package Geoexplore.Journey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import Geoexplore.Report.Report;
import java.util.List;

@Entity
@Table(name = "Stop")
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String coordinate;

    @ManyToOne
    @JoinColumn(name = "journeyID", nullable = false)
    @JsonBackReference
    private Journey journey;



//    @OneToMany(mappedBy = "stop", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Report> reports;

    // Costruttore vuoto richiesto da JPA
    public Stop() {}

    // Costruttore con parametri
    public Stop(String nome, String descrizione, String categoria, String coordinate, Journey journey) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.coordinate = coordinate;
        this.journey = journey;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }



//    public List<Report> getReports() {
//        return reports;
//    }
//
//    public void setReports(List<Report> reports) {
//        this.reports = reports;
//    }
}
