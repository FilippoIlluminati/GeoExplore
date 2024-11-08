// src/main/java/org/geoexplore/models/users/PlatformManager.java
package org.geoexplore.models.users;

import org.geoexplore.models.users.User;

public class PlatformManager extends User {

    public PlatformManager(String nomeUtente, String email) {
        super(nomeUtente, email);
    }

    @Override
    public void interagisci() {
        System.out.println("Il Platform Manager " + nomeUtente + " sta gestendo la piattaforma.");
    }

    public void autorizzaUtente(User user) {
        System.out.println("L'utente " + user.getNomeUtente() + " è stato autorizzato da " + nomeUtente);
    }

    public void revocaAutorizzazione(User user) {
        System.out.println("L'autorizzazione dell'utente " + user.getNomeUtente() + " è stata revocata da " + nomeUtente);
    }
}
