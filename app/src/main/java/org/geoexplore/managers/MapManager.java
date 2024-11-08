// src/main/java/org/geoexplore/managers/MapManager.java
package org.geoexplore.managers;

import org.geoexplore.models.Mappa;
import org.geoexplore.models.poi.InfoPoint;

public class MapManager {
    private Mappa mappa;

    public MapManager(Mappa mappa) {
        this.mappa = mappa;
    }

    public void aggiungiInfoPoint(InfoPoint infoPoint) {
        mappa.aggiungiInfoPoint(infoPoint);
        System.out.println("Aggiunto POI: " + infoPoint.getNome() + " alla mappa.");
    }

    public void rimuoviInfoPoint(InfoPoint infoPoint) {
        mappa.rimuoviInfoPoint(infoPoint);
        System.out.println("Rimosso POI: " + infoPoint.getNome() + " dalla mappa.");
    }

    public void visualizzaMappa() {
        mappa.visualizzaMappa();
    }
}
