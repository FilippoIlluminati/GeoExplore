package Geoexplore.Content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import Geoexplore.POI.POI;
import Geoexplore.Contest.Contest;
import Geoexplore.User.Users;

@Entity
@Table(name = "contents")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;

    @Column(length = 2048)
    private String descrizione;

    private String multimediaUrl;

    @Enumerated(EnumType.STRING)
    private ContentType contentType = ContentType.POI;

    private LocalDateTime dataCreazione;

    @Enumerated(EnumType.STRING)
    private ContentStatus status = ContentStatus.IN_ATTESA;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    @JsonIgnoreProperties("contents")
    private POI poi;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    @JsonIgnoreProperties("contents")
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonIgnoreProperties("contents")
    private Users creator;

    public Content() {}

    public Content(String titolo,
                   String descrizione,
                   String multimediaUrl,
                   ContentType contentType,
                   POI poi) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.multimediaUrl = multimediaUrl;
        this.contentType = contentType;
        this.poi = poi;
        this.dataCreazione = LocalDateTime.now();
        this.status = ContentStatus.IN_ATTESA;
    }

    public Long getId() { return id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public String getMultimediaUrl() { return multimediaUrl; }
    public void setMultimediaUrl(String multimediaUrl) { this.multimediaUrl = multimediaUrl; }
    public ContentType getContentType() { return contentType; }
    public void setContentType(ContentType contentType) { this.contentType = contentType; }
    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
    public ContentStatus getStatus() { return status; }
    public void setStatus(ContentStatus status) { this.status = status; }
    public POI getPoi() { return poi; }
    public void setPoi(POI poi) { this.poi = poi; }
    public Contest getContest() { return contest; }
    public void setContest(Contest contest) { this.contest = contest; }
    public Users getCreator() { return creator; }
    public void setCreator(Users creator) { this.creator = creator; }
}
