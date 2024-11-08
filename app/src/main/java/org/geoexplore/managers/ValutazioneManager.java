// src/main/java/org/geoexplore/managers/ValutazioneManager.java
package org.geoexplore.managers;

import org.geoexplore.models.Valutazione;
import java.util.ArrayList;
import java.util.List;

public class ValutazioneManager {
    private List<Valutazione> valutazioni;

    public ValutazioneManager() {
        this.valutazioni = new ArrayList<>();
    }

    public void aggiungiValutazione(Valutazione valutazione) {
        valutazioni.add(valutazione);
        System.out.println("Aggiunta valutazione di " + valutazione.getAutore() + " con punteggio: " + valutazione.getPunteggio());
    }

    public void visualizzaValutazioni() {
        System.out.println("Lista delle valutazioni:");
        for (Valutazione valutazione : valutazioni) {
            valutazione.visualizzaValutazione();
        }
    }
}
