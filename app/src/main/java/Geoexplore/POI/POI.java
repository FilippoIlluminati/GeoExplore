package Geoexplore.POI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import Geoexplore.User.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "poi")
public class POI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(length = 1024)
    private String descrizione;

    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private Category categoria;

    private String comune;

    // Ignora la serializzazione del creatore in POI per evitare la ricorsione
    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Users creator;

    private boolean approvato = false;

    // Costruttore vuoto richiesto da JPA
    public POI() {}

    // Costruttore con parametri
    public POI(String nome, String descrizione, double latitude, double longitude, Category categoria, String comune, Users creator, boolean approvato) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoria = categoria;
        this.comune = comune;
        this.creator = creator;
        this.approvato = approvato;
    }

    // Getters & Setters
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Category getCategoria() {
        return categoria;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public boolean isApprovato() {
        return approvato;
    }

    public void setApprovato(boolean approvato) {
        this.approvato = approvato;
    }
}
