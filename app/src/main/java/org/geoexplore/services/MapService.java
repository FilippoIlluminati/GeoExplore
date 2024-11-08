// src/main/java/org/geoexplore/services/MapService.java
package org.geoexplore.services;

import org.geoexplore.models.poi.InfoPoint;

public class MapService {

    public void visualizzaInfoPointSuMappa(InfoPoint infoPoint) {
        System.out.println("Visualizzazione del POI: " + infoPoint.getNome() +
                " su una mappa (Latitudine: " + infoPoint.getLatitudine() +
                ", Longitudine: " + infoPoint.getLongitudine() + ")");
        // Logica per integrare con un sistema di mappe esterno come OSM
    }

    public void cercaLuogo(String nomeLuogo) {
        System.out.println("Ricerca del luogo: " + nomeLuogo + " tramite il sistema di mappe.");
        // Logica per cercare un luogo tramite un sistema di mappe esterno
    }
}
