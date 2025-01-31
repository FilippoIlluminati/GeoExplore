package Geoexplore.User;

import jakarta.persistence.*;
import Geoexplore.Journey.Journey;
import Geoexplore.Content.Content;
import Geoexplore.Notification.Notification;
import Geoexplore.Contest.Contest;
import Geoexplore.Event.Event;
import Geoexplore.Report.Report;
import java.util.List;

@Entity
@Table(name = "users") // Specifica il nome della tabella
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false) // Campo per la password
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole ruolo;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Journey> journeys;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contest> contests;

    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    // Costruttore con parametri
    public Users(String nome, String cognome, String email, String username, String password, UserRole ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    // Costruttore vuoto richiesto da JPA
    public Users() {
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password; // Getter per la password
    }

    public void setPassword(String password) {
        this.password = password; // Setter per la password
    }

    public UserRole getRuolo() {
        return ruolo;
    }

    public void setRuolo(UserRole ruolo) {
        this.ruolo = ruolo;
    }
}
