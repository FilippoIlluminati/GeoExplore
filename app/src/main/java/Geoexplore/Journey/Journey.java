package Geoexplore.Journey;

import Geoexplore.User.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    @OneToMany(mappedBy = "journey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Stop> stops;

    // Indica se il journey è stato confermato
    private boolean confermato = false;

    // Costruttore vuoto richiesto da JPA
    public Journey() {
    }

    // Costruttore con parametri
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

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public boolean isConfermato() {
        return confermato;
    }

    public void setConfermato(boolean confermato) {
        this.confermato = confermato;
    }
}
