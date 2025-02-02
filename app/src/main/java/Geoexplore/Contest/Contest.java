package Geoexplore.Contest;

import Geoexplore.User.Users;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Contest")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    // Campo obbligatorio: obiettivo del contest
    @Column(nullable = false)
    private String obiettivo;

    // Campo facoltativo: descrizione aggiuntiva
    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    // L’organizzatore del contest: deve essere un animatore o il gestore della piattaforma
    @ManyToOne
    @JoinColumn(name = "organizzatoreID", nullable = false)
    private Users organizzatore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContestStatus status;

    // Set dei partecipanti (utenti) che si iscrivono al contest
    @ManyToMany
    @JoinTable(
            name = "contest_participants",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> partecipanti = new HashSet<>();

    // Costruttore vuoto richiesto da JPA
    public Contest() {}

    // Costruttore completo
    public Contest(String titolo, String obiettivo, String descrizione, LocalDate dataInizio, LocalDate dataFine, Users organizzatore, ContestStatus status) {
        this.titolo = titolo;
        this.obiettivo = obiettivo;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.organizzatore = organizzatore;
        this.status = status;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getObiettivo() {
        return obiettivo;
    }

    public void setObiettivo(String obiettivo) {
        this.obiettivo = obiettivo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public Users getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(Users organizzatore) {
        this.organizzatore = organizzatore;
    }

    public ContestStatus getStatus() {
        return status;
    }

    public void setStatus(ContestStatus status) {
        this.status = status;
    }

    public Set<Users> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(Set<Users> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public void addPartecipante(Users user) {
        this.partecipanti.add(user);
    }
}
