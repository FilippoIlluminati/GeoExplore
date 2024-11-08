// src/main/java/org/geoexplore/models/Itinerary.java
package org.geoexplore.models;

import org.geoexplore.models.poi.InfoPoint;
import java.util.ArrayList;
import java.util.List;

public class Itinerary {
    private String nome;
    private List<InfoPoint> puntiDiInteresse;

    public Itinerary(String nome) {
        this.nome = nome;
        this.puntiDiInteresse = new ArrayList<>();
    }

    public void aggiungiInfoPoint(InfoPoint infoPoint) {
        puntiDiInteresse.add(infoPoint);
    }

    public void rimuoviInfoPoint(InfoPoint infoPoint) {
        puntiDiInteresse.remove(infoPoint);
    }

    public void visualizzaItinerario() {
        System.out.println("Itinerario: " + nome);
        for (InfoPoint infoPoint : puntiDiInteresse) {
            infoPoint.mostraDettagli();
        }
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<InfoPoint> getPuntiDiInteresse() {
        return puntiDiInteresse;
    }
}
