// src/main/java/org/geoexplore/models/users/Tourist.java
package org.geoexplore.models.users;

import org.geoexplore.models.Content;

public class Tourist extends User {

    public Tourist(String nomeUtente, String email) {
        super(nomeUtente, email);
    }

    @Override
    public void interagisci() {
        System.out.println("Il turista " + nomeUtente + " sta esplorando la piattaforma.");
    }

    public void segnalaContenuto(Content content) {
        System.out.println(nomeUtente + " ha segnalato il contenuto: " + content.getDescrizione());
        // Logica per segnalare il contenuto al curatore potrebbe essere aggiunta qui
    }
}
