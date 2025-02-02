package Geoexplore.Notification;

import Geoexplore.User.Users;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications") // Nome tabella al plurale per standard SQL
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Users utente;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String testo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus stato;

    @Column(nullable = false, updatable = false) // Data di creazione
    private LocalDateTime dataCreazione;

    // Costruttore vuoto richiesto da JPA
    public Notification() {
        this.dataCreazione = LocalDateTime.now(); // Imposta automaticamente la data
    }

    // Costruttore con parametri
    public Notification(Users utente, String testo, NotificationStatus stato) {
        this.utente = utente;
        this.testo = testo;
        this.stato = stato;
        this.dataCreazione = LocalDateTime.now();
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUtente() {
        return utente;
    }

    public void setUtente(Users utente) {
        this.utente = utente;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public NotificationStatus getStato() {
        return stato;
    }

    public void setStato(NotificationStatus stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
}
