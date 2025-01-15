package Geoexplore.User;

import jakarta.persistence.*;

@Entity
public class Users {
    @Id
    String id;
    String nome;
    String cognome;
    String email;
    String username;
    @Enumerated(EnumType.STRING)
    private UserRole ruolo;


    public Users(String id, String nome, String cognome, String email, String username, UserRole ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.ruolo=ruolo;
    }

    public Users() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome(){
        return cognome;
    }

    public void setCognome(String cognome){
        this.cognome=cognome;
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



}

