package Geoexplore.Journey;

import Geoexplore.User.Users;
import jakarta.persistence.*;

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
    private Users creator;

    // Costruttore vuoto richiesto da JPA
    public Journey() {}

    // Costruttore con parametri
    public Journey(String nome, String descrizione, Users creator) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.creator = creator;
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

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }
}
