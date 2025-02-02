package Geoexplore.Content;

import com.fasterxml.jackson.annotation.JsonBackReference;
import Geoexplore.POI.POI;
import Geoexplore.User.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "contents")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false, length = 1024)
    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    // Se il contenuto è associato a un POI, questo campo viene valorizzato; altrimenti è null (contenuto generico)
    @ManyToOne(optional = true)
    @JoinColumn(name = "poi_id")
    private POI poi;

    // Il creatore del contenuto (obbligatorio)
    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonBackReference  // Interrompe il ciclo con Users.contents (che nel lato "padre" deve avere @JsonManagedReference)
    private Users creator;

    // Relazione one-to-one con Approval: tiene traccia dell'approvazione del contenuto
    @OneToOne(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private Approval approval;

    // Costruttore vuoto (richiesto da JPA)
    public Content() {}

    // Costruttore per contenuti associati a un POI
    public Content(String titolo, String descrizione, POI poi, Users creator) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.poi = poi;
        this.creator = creator;
        this.contentType = (poi != null) ? ContentType.POI : ContentType.GENERIC;
    }

    // Costruttore per contenuti generici
    public Content(String titolo, String descrizione, Users creator) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.creator = creator;
        this.contentType = ContentType.GENERIC;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
        this.contentType = (poi != null) ? ContentType.POI : ContentType.GENERIC;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public Approval getApproval() {
        return approval;
    }

    public void setApproval(Approval approval) {
        this.approval = approval;
    }
}
