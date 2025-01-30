package Geoexplore.User;

import jakarta.persistence.*;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole ruolo;

    // Costruttore con parametri
    public Users(String nome, String cognome, String email, String username, UserRole ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
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

    public UserRole getRuolo() {
        return ruolo;
    }

    public void setRuolo(UserRole ruolo) {
        this.ruolo = ruolo;
    }
}
