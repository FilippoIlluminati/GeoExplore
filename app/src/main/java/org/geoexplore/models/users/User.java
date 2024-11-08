// src/main/java/org/geoexplore/models/users/User.java
package org.geoexplore.models.users;

public abstract class User {
    protected String nomeUtente;
    protected String email;

    public User(String nomeUtente, String email) {
        this.nomeUtente = nomeUtente;
        this.email = email;
    }

    public abstract void interagisci();

    // Getter e Setter
    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
