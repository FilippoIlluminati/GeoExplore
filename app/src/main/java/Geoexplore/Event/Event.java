package Geoexplore.Event;

import Geoexplore.User.Users;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String luogo;

    @Column(nullable = false)
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "organizzatoreID", nullable = false)
    private Users organizzatore;

    // Costruttore vuoto richiesto da JPA
    public Event() {}

    // Costruttore con parametri
    public Event(String nome, String descrizione, LocalDate data, String luogo, String categoria, Users organizzatore) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.categoria = categoria;
        this.organizzatore = organizzatore;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Users getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(Users organizzatore) {
        this.organizzatore = organizzatore;
    }
}