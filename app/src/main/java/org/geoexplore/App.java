package org.geoexplore;

import org.geoexplore.factories.InfoPointFactory;
import org.geoexplore.managers.MapManager;
import org.geoexplore.models.Mappa;
import org.geoexplore.models.poi.InfoPoint;

public class App {
    public static void main(String[] args) {
        // Crea una nuova mappa per la città di Roma
        Mappa mappa = new Mappa("Roma", 100);

        // Inizializza il MapManager con la mappa
        MapManager mapManager = new MapManager(mappa);

        // Creazione di un punto di interesse fisico (es. il Colosseo)
        try {
            InfoPoint colosseo = InfoPointFactory.createInfoPoint("Physical", "Colosseo", "Antico anfiteatro", 41.8902, 12.4922);
            mapManager.aggiungiInfoPoint(colosseo);
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
        }

        // Creazione di un punto di interesse logico (es. comune di Roma)
        try {
            InfoPoint comuneRoma = InfoPointFactory.createInfoPoint("Logical", "Comune di Roma", "Centro amministrativo", 41.9028, 12.4964);
            mapManager.aggiungiInfoPoint(comuneRoma);
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
        }

        // Visualizza la mappa con i dettagli dei POI
        mapManager.visualizzaMappa();
    }
}
