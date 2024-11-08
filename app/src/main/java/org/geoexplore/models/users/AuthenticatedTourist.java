// src/main/java/org/geoexplore/models/users/AuthenticatedTourist.java
package org.geoexplore.models.users;

import org.geoexplore.models.Content;

public class AuthenticatedTourist extends Tourist {

    public AuthenticatedTourist(String nomeUtente, String email) {
        super(nomeUtente, email);
    }

    @Override
    public void interagisci() {
        System.out.println("Il turista autenticato " + nomeUtente + " sta esplorando la piattaforma e salvando preferenze.");
    }

    public void salvaInformazione(String informazione) {
        System.out.println(nomeUtente + " ha salvato l'informazione: " + informazione);
        // Logica per salvare informazioni per future visite
    }

    public Content caricaFoto(String descrizione) {
        System.out.println(nomeUtente + " ha caricato una nuova foto per approvazione.");
        return new Content("immagine", descrizione); // Foto in attesa di approvazione
    }
}
