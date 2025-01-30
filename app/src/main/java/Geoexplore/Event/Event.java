package Geoexplore.Event;

import jakarta.persistence.*;

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
    private String data;

    @Column(nullable = false)
    private String luogo;

    @Column(nullable = false)
    private String categoria;

    // Costruttore vuoto richiesto da JPA
    public Event() {}

    // Costruttore con parametri
    public Event(String nome, String descrizione, String data, String luogo, String categoria) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.categoria = categoria;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
}
