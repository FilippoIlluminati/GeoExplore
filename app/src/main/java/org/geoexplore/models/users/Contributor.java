// src/main/java/org/geoexplore/models/users/Contributor.java
package org.geoexplore.models.users;

import org.geoexplore.models.Content;

public class Contributor extends User {

    public Contributor(String nomeUtente, String email) {
        super(nomeUtente, email);
    }

    @Override
    public void interagisci() {
        System.out.println("Il contributor " + nomeUtente + " sta interagendo con la piattaforma.");
    }

    public Content caricaContenuto(String tipo, String descrizione) {
        System.out.println(nomeUtente + " ha caricato un nuovo contenuto.");
        return new Content(tipo, descrizione);
    }
}
