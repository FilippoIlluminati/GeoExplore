package Geoexplore.User;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String email;
    private String username;

    // Usare JsonProperty per permettere la deserializzazione della password (senza mostrarla nelle risposte)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole ruolo;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.IN_ATTESA;

    // Costruttore vuoto (necessario per JPA)
    public Users() {}

    // Costruttore senza AccountStatus
    public Users(String nome, String cognome, String email, String username, String password, UserRole ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        if (ruolo == UserRole.CONTRIBUTOR) {
            this.accountStatus = AccountStatus.IN_ATTESA;
        } else {
            this.accountStatus = AccountStatus.ATTIVO;
        }
    }

    // Costruttore con AccountStatus
    public Users(String nome, String cognome, String email, String username, String password, UserRole ruolo, AccountStatus accountStatus) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        this.accountStatus = accountStatus;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRuolo() { return ruolo; }
    public void setRuolo(UserRole ruolo) { this.ruolo = ruolo; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; }
}
