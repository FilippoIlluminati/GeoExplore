// src/main/java/org/geoexplore/managers/ContentManager.java
package org.geoexplore.managers;

import org.geoexplore.models.Content;
import java.util.ArrayList;
import java.util.List;

public class ContentManager {
    private List<Content> contenuti;

    public ContentManager() {
        this.contenuti = new ArrayList<>();
    }

    public void aggiungiContenuto(Content content) {
        contenuti.add(content);
        System.out.println("Aggiunto contenuto: " + content.getDescrizione());
    }

    public void approvaContenuto(Content content) {
        content.approvaContenuto();
        System.out.println("Contenuto approvato: " + content.getDescrizione());
    }

    public void visualizzaContenuti() {
        System.out.println("Lista dei contenuti:");
        for (Content content : contenuti) {
            content.visualizzaContenuto();
        }
    }
}
