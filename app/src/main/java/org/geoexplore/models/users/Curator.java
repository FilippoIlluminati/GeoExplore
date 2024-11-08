// src/main/java/org/geoexplore/models/users/Curator.java
package org.geoexplore.models.users;

import org.geoexplore.models.Content;

public class Curator extends User {

    public Curator(String nomeUtente, String email) {
        super(nomeUtente, email);
    }

    @Override
    public void interagisci() {
        System.out.println("Il curatore " + nomeUtente + " sta interagendo con la piattaforma.");
    }

    public void approvaContenuto(Content content) {
        content.approvaContenuto();
        System.out.println("Il contenuto è stato approvato da " + nomeUtente);
    }

    public void rifiutaContenuto(Content content) {
        System.out.println("Il contenuto è stato rifiutato da " + nomeUtente);
    }
}
