package Geoexplore.Contest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import Geoexplore.User.Users;

@Entity
@Table(name = "partecipazioni_concorso")
public class ContestEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String contenuto;

    private LocalDateTime dataInvio = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatoPartecipazione stato = StatoPartecipazione.IN_ATTESA;

    @ManyToOne
    @JoinColumn(name = "concorso_id")
    @JsonBackReference
    private Contest concorso;

    @ManyToOne
    @JoinColumn(name = "partecipante_id")
    private Users partecipante;

    public ContestEntry() {}

    public ContestEntry(String contenuto, Contest concorso, Users partecipante) {
        this.contenuto = contenuto;
        this.concorso = concorso;
        this.partecipante = partecipante;
        this.stato = StatoPartecipazione.IN_ATTESA;
    }

    public Long getId() { return id; }
    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }
    public LocalDateTime getDataInvio() { return dataInvio; }
    public void setDataInvio(LocalDateTime dataInvio) { this.dataInvio = dataInvio; }
    public StatoPartecipazione getStato() { return stato; }
    public void setStato(StatoPartecipazione stato) { this.stato = stato; }
    public Contest getConcorso() { return concorso; }
    public void setConcorso(Contest concorso) { this.concorso = concorso; }
    public Users getPartecipante() { return partecipante; }
    public void setPartecipante(Users partecipante) { this.partecipante = partecipante; }
}
