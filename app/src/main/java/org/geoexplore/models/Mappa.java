// src/main/java/org/geoexplore/models/Mappa.java
package org.geoexplore.models;

import java.util.ArrayList;
import java.util.List;
import org.geoexplore.models.poi.InfoPoint;


public class Mappa {
    private String nomeCitta;
    private int dimensioni; // Può rappresentare l'estensione della mappa
    private List<InfoPoint> puntiDiInteresse;

    public Mappa(String nomeCitta, int dimensioni) {
        this.nomeCitta = nomeCitta;
        this.dimensioni = dimensioni;
        this.puntiDiInteresse = new ArrayList<>();
    }

    public void aggiungiInfoPoint(InfoPoint infoPoint) {
        puntiDiInteresse.add(infoPoint);
    }

    public void rimuoviInfoPoint(InfoPoint infoPoint) {
        puntiDiInteresse.remove(infoPoint);
    }

    public void visualizzaMappa() {
        System.out.println("Mappa della città: " + nomeCitta);
        for (InfoPoint infoPoint : puntiDiInteresse) {
            infoPoint.mostraDettagli();
        }
    }

    // Getter e setter se necessari
}
