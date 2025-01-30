package Geoexplore.Content;

import Geoexplore.POI.POI;
import Geoexplore.User.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "Content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // Testuale o Multimediale

    @Column(nullable = false)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String testo;

    @Column
    private String mediaPath;

    @ManyToOne
    @JoinColumn(name = "stopID", nullable = false)
    private POI stop;

    @Column(nullable = false)
    private Boolean approved = false;

    @ManyToOne
    @JoinColumn(name = "creatorID", nullable = false)
    private Users creator;

    // Costruttore vuoto richiesto da JPA
    public Content() {}

    // Costruttore con parametri
    public Content(String tipo, String titolo, String testo, String mediaPath, POI stop, Users creator) {
        this.tipo = tipo;
        this.titolo = titolo;
        this.testo = testo;
        this.mediaPath = mediaPath;
        this.stop = stop;
        this.creator = creator;
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

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public POI getStop() {
        return stop;
    }

    public void setStop(POI stop) {
        this.stop = stop;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }
}
