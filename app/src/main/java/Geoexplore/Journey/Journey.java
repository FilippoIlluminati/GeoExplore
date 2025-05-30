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

    private boolean confermato = false;
    private boolean ordinato;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "journey_poi",
            joinColumns = @JoinColumn(name = "journey_id"),
            inverseJoinColumns = @JoinColumn(name = "poi_id")
    )
    @OrderColumn(name = "sequence")
    private List<POI> poiList;

    public Journey() {
    }

    public Journey(String nome, String descrizione, Users creator) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.creator = creator;
        this.confermato = false;
    }

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
