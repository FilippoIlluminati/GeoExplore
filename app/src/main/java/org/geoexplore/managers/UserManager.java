// src/main/java/org/geoexplore/managers/UserManager.java
package org.geoexplore.managers;

import org.geoexplore.models.users.User;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> utenti;

    public UserManager() {
        this.utenti = new ArrayList<>();
    }

    public void aggiungiUtente(User user) {
        utenti.add(user);
        System.out.println("Aggiunto utente: " + user.getNomeUtente());
    }

    public void rimuoviUtente(User user) {
        utenti.remove(user);
        System.out.println("Rimosso utente: " + user.getNomeUtente());
    }

    public void visualizzaUtenti() {
        System.out.println("Lista degli utenti:");
        for (User user : utenti) {
            System.out.println("- " + user.getNomeUtente());
        }
    }
}
