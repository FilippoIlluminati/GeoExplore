package Geoexplore.Journey;

import Geoexplore.User.Users;
import Geoexplore.POI.POI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Journey")
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "creatorID", nullable = false)
    @JsonIgnoreProperties("journeys")
    private Users creator;

    // Stato di conferma (approvazione)
    private boolean confermato = false;

    // Indica se il journey è ordinato (true) o non ordinato (false)
    private boolean ordinato;

    // Lista dei POI associati – utilizziamo FetchType.EAGER per caricare tutti i dati dei POI
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "journey_poi",
            joinColumns = @JoinColumn(name = "journey_id"),
            inverseJoinColumns = @JoinColumn(name = "poi_id")
    )
    @OrderColumn(name = "sequence")
    private List<POI> poiList;

    // Costruttore vuoto richiesto da JPA
    public Journey() {
    }

    // Costruttore con parametri (senza poiList e ordinato, da impostare separatamente)
    public Journey(String nome, String descrizione, Users creator) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.creator = creator;
        this.confermato = false;
    }

    // Getters e Setters
    public Long getId() {
        return id;
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

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public boolean isConfermato() {
        return confermato;
    }

    public void setConfermato(boolean confermato) {
        this.confermato = confermato;
    }

    public boolean isOrdinato() {
        return ordinato;
    }

    public void setOrdinato(boolean ordinato) {
        this.ordinato = ordinato;
    }

    public List<POI> getPoiList() {
        return poiList;
    }

    public void setPoiList(List<POI> poiList) {
        this.poiList = poiList;
    }
}
