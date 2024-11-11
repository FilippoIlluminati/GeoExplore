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

        // Creazione e aggiunta di punti di interesse
        aggiungiPuntoDiInteresse(mapManager, "Physical", "Colosseo", "Antico anfiteatro", 41.8902, 12.4922);
        aggiungiPuntoDiInteresse(mapManager, "Logical", "Comune di Roma", "Centro amministrativo", 41.9028, 12.4964);

        // Visualizza la mappa con i dettagli dei POI
        mapManager.visualizzaMappa();
    }

    private static void aggiungiPuntoDiInteresse(MapManager mapManager, String tipo, String nome, String descrizione, double latitudine, double longitudine) {
        try {
            InfoPoint puntoDiInteresse = InfoPointFactory.createInfoPoint(tipo, nome, descrizione, latitudine, longitudine);
            mapManager.aggiungiInfoPoint(puntoDiInteresse);
        } catch (IllegalArgumentException e) {
            System.out.println("Errore durante l'aggiunta di " + nome + ": " + e.getMessage());
        }
    }
}
