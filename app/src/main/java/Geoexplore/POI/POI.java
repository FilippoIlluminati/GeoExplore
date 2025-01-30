package Geoexplore.POI;

import jakarta.persistence.*;

@Entity
@Table(name = "POI")
public class POI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String coordinate;

    // Costruttore vuoto richiesto da JPA
    public POI() {}

    // Costruttore con parametri
    public POI(String nome, String descrizione, String categoria, String coordinate) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.coordinate = coordinate;
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
}