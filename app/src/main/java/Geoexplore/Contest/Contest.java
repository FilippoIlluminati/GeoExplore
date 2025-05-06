package Geoexplore.Contest;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import Geoexplore.User.Users;

@Entity
@Table(name = "concorsi")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String descrizione;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;

    @Enumerated(EnumType.STRING)
    private StatoConcorso stato = StatoConcorso.BOZZA;

    // Indica se il concorso è a invito
    private boolean invitazionale = false;

    // Elenco degli ID utenti invitati (se presente)
    @ElementCollection
    private List<Long> invitedUserIds;

    // Creatore del concorso (utente con ruolo ANIMATORE)
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Users creatore;

    // Partecipazioni al concorso
    @OneToMany(mappedBy = "concorso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ContestEntry> partecipazioni;

    public Contest() {}

    public Contest(String titolo, String descrizione, LocalDateTime dataInizio, LocalDateTime dataFine,
                   boolean invitazionale, List<Long> invitedUserIds, Users creatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.invitazionale = invitazionale;
        this.invitedUserIds = invitedUserIds;
        this.creatore = creatore;
        this.stato = StatoConcorso.BOZZA;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public LocalDateTime getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDateTime dataInizio) { this.dataInizio = dataInizio; }
    public LocalDateTime getDataFine() { return dataFine; }
    public void setDataFine(LocalDateTime dataFine) { this.dataFine = dataFine; }
    public StatoConcorso getStato() { return stato; }
    public void setStato(StatoConcorso stato) { this.stato = stato; }
    public boolean isInvitazionale() { return invitazionale; }
    public void setInvitazionale(boolean invitazionale) { this.invitazionale = invitazionale; }
    public List<Long> getInvitedUserIds() { return invitedUserIds; }
    public void setInvitedUserIds(List<Long> invitedUserIds) { this.invitedUserIds = invitedUserIds; }
    public Users getCreatore() { return creatore; }
    public void setCreatore(Users creatore) { this.creatore = creatore; }
    public List<ContestEntry> getPartecipazioni() { return partecipazioni; }
    public void setPartecipazioni(List<ContestEntry> partecipazioni) { this.partecipazioni = partecipazioni; }
}
