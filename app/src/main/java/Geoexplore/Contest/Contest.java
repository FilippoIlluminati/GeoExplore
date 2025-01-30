package Geoexplore.Contest;

import Geoexplore.User.Users;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Contest")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    @ManyToOne
    @JoinColumn(name = "organizzatoreID", nullable = false)
    private Users organizzatore;

    // Costruttore vuoto richiesto da JPA
    public Contest() {}

    // Costruttore con parametri
    public Contest(String titolo, String descrizione, LocalDate dataInizio, LocalDate dataFine, Users organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.organizzatore = organizzatore;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
